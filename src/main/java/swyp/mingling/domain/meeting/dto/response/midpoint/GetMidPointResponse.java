package swyp.mingling.domain.meeting.dto.response.midpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
// 2. 하나의 추천 장소에 대한 전체 요약 (최종 결과용)
public class GetMidPointResponse {

    @Schema(description = "도착역", example = "사당역")
    private String endStation;

    @Schema(description = "도착역 호선", example = "4호선")
    private String endStationLine;

    @Schema(description = "위도", example = "37.5567")
    private double latitude;

    @Schema(description = "경도", example = "126.9236")
    private double longitude;

    @Schema(description = "사용자 목록")
    List<UserRouteDto> userRoutes;
}