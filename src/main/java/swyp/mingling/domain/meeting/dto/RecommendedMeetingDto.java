package swyp.mingling.domain.meeting.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
// 2. 하나의 추천 장소에 대한 전체 요약 (최종 결과용)
public class RecommendedMeetingDto {
    String endStation;
    List<UserRouteDto> userRoutes;
}