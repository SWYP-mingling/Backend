package swyp.mingling.domain.subway.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import swyp.mingling.domain.subway.dto.SubwayRouteInfo;
import swyp.mingling.external.dto.response.SeoulMetroRouteResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 서울시 지하철 API 응답을 파싱하여 필요한 정보만 추출하는 Parser
 * 복잡한 JSON 구조에서 시간, 거리, 역명, 환승 정보를 추출
 */
@Slf4j
@Component
public class SeoulMetroRouteParser {

    /**
     * 서울시 API 응답을 간소화된 경로 정보로 변환
     *
     * @param response 서울시 지하철 API 응답
     * @return 간소화된 경로 정보
     */
    public SubwayRouteInfo parse(SeoulMetroRouteResponse response) {
        if (response == null || response.getPathInfoList().isEmpty()) {
            log.warn("서울시 API 응답이 비어있습니다.");
            return null;
        }

        List<SeoulMetroRouteResponse.PathInfo> pathList = response.getPathInfoList();

        // 출발역 = 첫 번째 경로의 출발역
        String startStation = pathList.get(0).getDptreStn().getStnNm();

        // 도착역 = 마지막 경로의 도착역
        String endStation = pathList.get(pathList.size() - 1).getArvlStn().getStnNm();

        // 총 이동 시간 (초 → 분 변환)
        Integer totalTimeInSeconds = response.getBody().getTotalreqHr();
        Integer totalTime = totalTimeInSeconds != null ? totalTimeInSeconds / 60 : 0;

        // 총 이동 거리 (미터 → km 변환)
        Integer totalDistanceInMeters = response.getBody().getTotalDstc();
        Double totalDistance = totalDistanceInMeters != null ? totalDistanceInMeters / 1000.0 : 0.0;

        // 환승 경로 생성 (예: "2호선 > 6호선")
        String transferPath = buildTransferPath(pathList);

        // 역별 상세 정보
        List<SubwayRouteInfo.StationInfo> stations = buildStationInfoList(pathList);

        return SubwayRouteInfo.builder()
                .startStation(startStation)
                .endStation(endStation)
                .totalTravelTime(totalTime)
                .totalDistance(totalDistance)
                .transferPath(transferPath)
                .stations(stations)
                .build();
    }

    /**
     * 역별 상세 정보 리스트 생성
     */
    private List<SubwayRouteInfo.StationInfo> buildStationInfoList(
            List<SeoulMetroRouteResponse.PathInfo> pathList) {

        List<SubwayRouteInfo.StationInfo> stations = new ArrayList<>();

        for (int i = 0; i < pathList.size(); i++) {
            SeoulMetroRouteResponse.PathInfo path = pathList.get(i);

            // 출발역 추가 (첫 번째 경로일 때만)
            if (i == 0) {
                stations.add(SubwayRouteInfo.StationInfo.builder()
                        .stationName(path.getDptreStn().getStnNm())
                        .lineNumber(formatLineNumber(path.getDptreStn().getLineNm()))
                        .travelTime(0)
                        .isTransfer(false)
                        .transferStationName(null)
                        .build());
            }

            // 도착역 추가
            Integer travelTimeInSeconds = path.getReqHr();
            Integer travelTimeInMinutes = travelTimeInSeconds != null ? travelTimeInSeconds / 60 : 0;

            stations.add(SubwayRouteInfo.StationInfo.builder()
                    .stationName(path.getArvlStn().getStnNm())
                    .lineNumber(formatLineNumber(path.getArvlStn().getLineNm()))
                    .travelTime(travelTimeInMinutes)
                    .isTransfer("Y".equalsIgnoreCase(path.getTrsitYn()))
                    .transferStationName("Y".equalsIgnoreCase(path.getTrsitYn()) ? path.getArvlStn().getStnNm() : null)
                    .build());
        }

        return stations;
    }

    /**
     * 환승 경로 문자열 생성
     * 예: "신분당선 > 2호선 > 1호선"
     *
     * @param pathList 경로 정보 리스트
     * @return 환승 경로 문자열
     */
    private String buildTransferPath(List<SeoulMetroRouteResponse.PathInfo> pathList) {
        List<String> lines = new ArrayList<>();
        String currentLine = null;

        // 첫 번째 경로의 출발역 호선 추가
        if (!pathList.isEmpty()) {
            String firstLine = pathList.get(0).getDptreStn().getLineNm();
            lines.add(formatLineNumber(firstLine));
            currentLine = firstLine;
        }

        // 각 경로의 도착역 호선 확인하여 변경 시 추가
        for (SeoulMetroRouteResponse.PathInfo path : pathList) {
            String line = path.getArvlStn().getLineNm();

            // 호선이 변경되었을 때만 추가 (중복 제거)
            if (!line.equals(currentLine)) {
                lines.add(formatLineNumber(line));
                currentLine = line;
            }
        }

        return String.join(" > ", lines);
    }

    /**
     * 호선 번호 포맷팅
     * 예: "1" -> "1호선", "K" -> "경의중앙선"
     *
     * @param lineNumber 호선 번호
     * @return 포맷팅된 호선명
     */
    private String formatLineNumber(String lineNumber) {
        if (lineNumber == null || lineNumber.isEmpty()) {
            return "";
        }

        // 숫자만 있는 경우 "호선" 추가
        if (lineNumber.matches("\\d+")) {
            return lineNumber + "호선";
        }

        // 이미 "호선"이 포함된 경우 그대로 반환
        if (lineNumber.contains("호선")) {
            return lineNumber;
        }

        // 특수 노선 (경의중앙선, 공항철도 등)은 그대로 반환
        return lineNumber;
    }
}
