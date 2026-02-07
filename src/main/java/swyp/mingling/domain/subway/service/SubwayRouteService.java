package swyp.mingling.domain.subway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swyp.mingling.domain.subway.dto.SubwayRouteInfo;
import swyp.mingling.domain.subway.parser.SeoulMetroRouteParser;
import swyp.mingling.external.SeoulMetroClient;
import swyp.mingling.external.dto.response.SeoulMetroRouteResponse;
import swyp.mingling.global.exception.BusinessException;
import swyp.mingling.global.exception.ErrorCode;

import java.util.List;

/**
 * 지하철 경로 조회 서비스
 * 서울시 API 호출 및 파싱을 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubwayRouteService {

    private final SeoulMetroClient seoulMetroClient;
    private final SeoulMetroRouteParser routeParser;

    public SubwayRouteInfo getRoute(String startStation, String endStation) {
        log.info("지하철 경로 조회 시작: {} -> {}", startStation, endStation);

        // 0. 출발역과 도착역이 같은지 체크 (역 이름 정규화 후 비교)
        String normalizedStart = normalizeStationName(startStation);
        String normalizedEnd = normalizeStationName(endStation);

        if (normalizedStart.equals(normalizedEnd)) {
            log.info("출발역과 도착역이 같습니다: {} -> {}, 소요시간: 0분", startStation, endStation);
            return SubwayRouteInfo.builder()
                    .startStation(startStation)
                    .startStationLine("")
                    .endStation(endStation)
                    .endStationLine("")
                    .totalTravelTime(0)
                    .totalDistance(0.0)
                    .transferCount(0)
                    .transferPath(List.of())
                    .stations(List.of())
                    .build();
        }

        int maxRetries = 10;
        int retryCount = 0;
        BusinessException lastException = null;

        while (retryCount < maxRetries) {
            try {
                // 1. API 호출
                SeoulMetroRouteResponse response = seoulMetroClient.searchRoute(startStation, endStation);

                // 응답 로그 추가
                if (response != null && response.getErrorCode() != null) {
                    log.info("API 응답 코드: {}, 메시지: {}", response.getErrorCode(), response.getErrorMessage());
                    log.info("경로 데이터 개수: {}", response.getPathInfoList().size());
                    log.info("Body 존재 여부: {}", response.getBody() != null);
                }

                // 2. 응답 검증 및 에러 처리
                validateResponse(response);

                // 3. Parser를 통한 데이터 변환
                SubwayRouteInfo routeInfo = routeParser.parse(response);

                log.info("지하철 경로 조회 완료: {} -> {}, 소요시간: {}분, 거리: {}km",
                        routeInfo.getStartStation(), routeInfo.getEndStation(),
                        routeInfo.getTotalTravelTime(), routeInfo.getTotalDistance());

                return routeInfo;

            } catch (BusinessException e) {
                lastException = e;
                retryCount++;

                // 재시도 가능한 에러인지 확인 (서버 에러만 재시도)
                if (e.getErrorCode() == ErrorCode.SUBWAY_API_SERVER_ERROR) {
                    if (retryCount < maxRetries) {
                        log.warn("API 호출 실패 (시도 {}/{}): {} -> {}. 재시도합니다...",
                                retryCount, maxRetries, startStation, endStation);
                        try {
                            Thread.sleep(1000); // 1초 대기
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw e;
                        }
                    } else {
                        log.error("API 호출 최대 재시도 횟수 초과 ({}/{}): {} -> {}",
                                retryCount, maxRetries, startStation, endStation);
                        throw e;
                    }
                } else {
                    // 재시도 불가능한 에러 (인증키 오류, 데이터 없음 등)는 즉시 throw
                    log.error("재시도 불가능한 에러 발생: {}", e.getErrorCode());
                    throw e;
                }
            }
        }

        // 최대 재시도 횟수 초과
        log.error("API 호출 최대 재시도 횟수 초과: {} -> {}", startStation, endStation);
        throw lastException;
    }

    /**
     * 역 이름 정규화 (역 제거)
     *
     * @param stationName 역 이름
     * @return 정규화된 역 이름
     */
    private String normalizeStationName(String stationName) {
        if (stationName == null) {
            return null;
        }
        // "서울역"은 예외 처리
        if ("서울역".equals(stationName)) {
            return stationName;
        }
        // "역" 제거
        return stationName.replaceAll("역$", "");
    }

    /**
     * 서울시 API 응답 검증 및 에러 처리
     *
     * @param response API 응답
     * @throws BusinessException API 에러 발생 시
     */
    private void validateResponse(SeoulMetroRouteResponse response) {
        // 응답 null 체크
        if (response == null) {
            log.error("서울시 지하철 API 응답이 null입니다.");
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR);
        }

        // header가 null이거나 body가 null인 경우 - 공공 데이터 API 오류
        if (response.getHeader() == null || response.getBody() == null) {
            log.error("공공 데이터 API 응답 형식 오류: header={}, body={}",
                    response.getHeader(), response.getBody());
            throw new BusinessException(ErrorCode.SUBWAY_API_SERVER_ERROR);
        }

        // 인증키 오류 (INFO-100)
        if (response.isInvalidApiKey()) {
            log.error("서울시 지하철 API 인증키 오류: {}", response.getErrorMessage());
            throw new BusinessException(ErrorCode.SUBWAY_API_INVALID_KEY);
        }

        // 필수 값 누락 (ERROR-300)
        if (response.isMissingParameter()) {
            log.error("서울시 지하철 API 필수 값 누락: {}", response.getErrorMessage());
            throw new BusinessException(ErrorCode.SUBWAY_API_MISSING_PARAMETER);
        }

        // 서비스 없음 (ERROR-310)
        if (response.isServiceNotFound()) {
            log.error("서울시 지하철 API 서비스 없음: {}", response.getErrorMessage());
            throw new BusinessException(ErrorCode.SUBWAY_API_SERVICE_NOT_FOUND);
        }

        // 서버 오류 (ERROR-500, ERROR-600, ERROR-601)
        if (response.isServerError()) {
            log.error("서울시 지하철 API 서버 오류: {} - {}", response.getErrorCode(), response.getErrorMessage());
            throw new BusinessException(ErrorCode.SUBWAY_API_SERVER_ERROR);
        }

        // 데이터 없음 (INFO-200)
        if (response.isNoData()) {
            log.warn("조회된 지하철 경로 데이터가 없습니다: {} - {}", response.getErrorCode(), response.getErrorMessage());
            throw new BusinessException(ErrorCode.SUBWAY_ROUTE_NOT_FOUND);
        }

        // 정상 응답이지만 경로 데이터가 비어있는 경우
        if (!response.isSuccess() || response.getPathInfoList().isEmpty()) {
            log.warn("조회된 지하철 경로 데이터가 없습니다.");
            throw new BusinessException(ErrorCode.SUBWAY_ROUTE_NOT_FOUND);
        }
    }
}
