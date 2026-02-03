package swyp.mingling.domain.meeting.dto;

import lombok.Builder;
import lombok.Getter;
import swyp.mingling.domain.subway.dto.SubwayRouteInfo;

import java.util.List;

@Getter
@Builder
// 1. 개별 사용자의 경로 요약 정보
public class UserRouteDto {
    String nickname;
    String startStation;
    int travelTime;
    List<SubwayRouteInfo.StationInfo> stations;
}