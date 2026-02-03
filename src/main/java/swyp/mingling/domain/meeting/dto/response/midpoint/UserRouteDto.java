package swyp.mingling.domain.meeting.dto.response.midpoint;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder

// 1. 개별 사용자의 경로 요약 정보
@JsonPropertyOrder({ "nickname", "startStation", "startStationLine", "latitude", "longitude", "travelTime", "transferPath", "stations" })
public class UserRouteDto {

    @Schema(description = "사용자 닉네임", example = "nick")
    private String nickname;

    @Schema(description = "출발역", example = "홍대입구역")
    private String startStation;

    @Schema(description = "출발역 호선", example = "2호선")
    private String startStationLine;

    @Schema(description = "위도", example = "37.5567")
    private double latitude;

    @Schema(description = "경도", example = "126.9236")
    private double longitude;

    @Schema(description = "걸리는 시간", example = "20")
    private int travelTime;

    @Schema(description = "환승 역 목록")
    private List<StationPathResponse> transferPath;

    @Schema(description = "지나가는 역 목록")
    private List<StationPathResponse> stations;
}