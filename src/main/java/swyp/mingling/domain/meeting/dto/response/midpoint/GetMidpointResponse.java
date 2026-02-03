package swyp.mingling.domain.meeting.dto.response.midpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 중간지점 조회 API 응답 DTO
 * 중간지점 목록과 모든 참여자의 이동경로 정보를 포함
 */
@Getter
@Builder
@AllArgsConstructor
public class GetMidpointResponse {

    @Schema(description = "중간지점 목록")
    private List<MidpointDto> midpoints;

    @Schema(description = "참여자별 환승경로")
    private List<ParticipantPath> participantPaths;

    /**
     * 중간지점 상세 정보 DTO
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class MidpointDto {
        @Schema(description = "중간지점 역 이름", example = "합정역")
        private String name;

        @Schema(description = "위도", example = "37.5484757")
        private Double latitude;

        @Schema(description = "경도", example = "126.912071")
        private Double longitude;

        @Schema(description = "참여자 평균 이동 시간 (분 단위)", example = "30")
        private Integer avgTravelTime;

        @Schema(description = "현재 로그인한 사용자의 환승 경로", example = "2호선 > 6호선")
        private String transferPath;
    }

    /**
     * 참여자별 환승경로 정보
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ParticipantPath {
        @Schema(description = "참여자 이름", example = "사용자A")
        private String userName;

        @Schema(description = "출발역", example = "구로디지털단지역")
        private String departureStation;

        @Schema(description = "환승 경로", example = "2호선 > 6호선")
        private String transferPath;

        @Schema(description = "총 이동 시간 (분)", example = "25")
        private Integer travelTime;

        @Schema(description = "경유 역 리스트", example = "[\"구로디지털단지\", \"신도림\", \"영등포구청\", \"당산\", \"합정\"]")
        private List<String> stationNames;
    }
}
