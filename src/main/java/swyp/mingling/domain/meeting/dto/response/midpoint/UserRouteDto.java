package swyp.mingling.domain.meeting.dto.response.midpoint;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder

// 1. 개별 사용자의 경로 요약 정보
@JsonPropertyOrder({ "nickname", "startStation", "latitude", "longitude", "travelTime", "transferPath", "stations" })
public class UserRouteDto {
    String nickname;
    String startStation;
    private double latitude;
    private double longitude;
    int travelTime;
    List<StationPathResponse> transferPath;
    List<StationPathResponse> stations;
}