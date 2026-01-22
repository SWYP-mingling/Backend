package swyp.mingling.domain.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetMidpointResponse {

    @Schema(description = "중간지점 목록")
    private List<MidpointDto> midpoints;

    /**
     * 중간지점 상세 정보 DTO
     */
    @Getter
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

        @Schema(description = "로그인 시 제공되는 환승 경로", example = "2호선 > 6호선")
        private String transferPath;
    }
}
