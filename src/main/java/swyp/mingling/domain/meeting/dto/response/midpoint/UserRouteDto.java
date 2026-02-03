package swyp.mingling.domain.meeting.dto.response.midpoint;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder

// 1. 개별 사용자의 경로 요약 정보
@JsonPropertyOrder({ "nickname", "startStation", "startStationLine", "latitude", "longitude", "travelTime", "transferPath", "stations" })
public class UserRouteDto {
    private String nickname;
    private String startStation;
    private String startStationLine;
    private double latitude;
    private double longitude;
    private int travelTime;
    private List<StationPathResponse> transferPath;
    private List<StationPathResponse> stations;
}