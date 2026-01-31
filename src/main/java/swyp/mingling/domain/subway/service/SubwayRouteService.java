package swyp.mingling.domain.subway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swyp.mingling.domain.subway.dto.SubwayRouteInfo;
import swyp.mingling.domain.subway.parser.SeoulMetroRouteParser;
import swyp.mingling.external.SeoulMetroClient;
import swyp.mingling.external.dto.response.SeoulMetroRouteResponse;

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

        // 1. API 호출
        SeoulMetroRouteResponse response = seoulMetroClient.searchRoute(startStation, endStation);

        // 2. 응답 데이터 존재 여부 검증
        if (response == null || !response.isSuccess() || response.getPathInfoList().isEmpty()) {
            log.warn("조회된 지하철 경로 데이터가 없습니다.");
            throw new RuntimeException("해당 구간의 지하철 경로 정보를 찾을 수 없습니다.");
        }

        // 3. Parser를 통한 데이터 변환
        SubwayRouteInfo routeInfo = routeParser.parse(response);

        log.info("지하철 경로 조회 완료: {} -> {}, 소요시간: {}분, 거리: {}km",
                routeInfo.getStartStation(), routeInfo.getEndStation(),
                routeInfo.getTotalTravelTime(), routeInfo.getTotalDistance());

        return routeInfo;
    }
}
