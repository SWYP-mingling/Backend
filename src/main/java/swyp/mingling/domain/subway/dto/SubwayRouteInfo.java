package swyp.mingling.domain.subway.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 지하철 경로 정보를 담는 간소화된 도메인 DTO
 * 서울시 API 응답에서 필요한 정보만 추출하여 사용
 */
@Getter
@Builder
@ToString
public class SubwayRouteInfo {

    private final String startStation;
    private final String endStation;
    private final Integer totalTravelTime;
    private final Double totalDistance;
    private final String transferPath;
    private final List<StationInfo> stations; // 이 부분이 Parser에서 사용됨

    /**
     * 경유 역 이름만 리스트로 반환
     * @return 경유 역 이름 리스트 (예: ["강남", "신논현", "논현", "신사"])
     */
    public List<String> getStationNames() {
        return stations.stream()
                .map(StationInfo::getStationName)
                .collect(Collectors.toList());
    }

    /**
     * 역별 상세 정보를 담는 내부 클래스
     */
    @Getter
    @Builder
    @ToString
    public static class StationInfo {
        private final String stationName;
        private final String lineNumber;
        private final Integer travelTime;
        private final boolean isTransfer;
        private final String transferStationName;
    }
}