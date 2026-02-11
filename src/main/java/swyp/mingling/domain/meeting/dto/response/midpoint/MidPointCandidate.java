package swyp.mingling.domain.meeting.dto.response.midpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import swyp.mingling.domain.subway.dto.SubwayRouteInfo;

import java.util.List;

@Getter
@AllArgsConstructor
public class MidPointCandidate {

    private List<SubwayRouteInfo> routes; // 해당 번화가로 가는 모든 사람의 경로
    private int deviation;                // 이동시간 편차
    private int avgTime;                  // 평균 이동시간 or 총합
    private boolean isHot;                // 가장 장소가 많은 곳
    private int placeCount;               // 주변 장소 개수

    public void setHot(boolean hot) {
        isHot = hot;
    }
}
