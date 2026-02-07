package swyp.mingling.domain.meeting.dto.response.midpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import swyp.mingling.domain.subway.dto.SubwayRouteInfo;

@Getter
@AllArgsConstructor
public class MidPointCandidate {

    private List<SubwayRouteInfo> routes; // 해당 번화가로 가는 모든 사람의 경로
    private int deviation;                // 이동시간 편차
    private int avgTime;                  // 평균 이동시간 or 총합
}
