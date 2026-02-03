package swyp.mingling.domain.meeting.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.mingling.domain.meeting.dto.StationCoordinate;
import swyp.mingling.domain.meeting.dto.response.midpoint.DepartureListResponse;
import swyp.mingling.domain.meeting.dto.response.midpoint.HotPlaceCategoryResponse;
import swyp.mingling.domain.meeting.dto.response.midpoint.StationPathResponse;
import swyp.mingling.domain.meeting.repository.HotPlaceRepository;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.domain.subway.dto.SubwayRouteInfo;
import swyp.mingling.domain.subway.service.SubwayRouteService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MidPointUseCase {

    private final MeetingRepository meetingRepository;
    private final FindStationCoordinateUseCase findStationCoordinateUseCase;
    private final HotPlaceRepository hotPlaceRepository;
    private final SubwayRouteService subwayRouteService;

    public List<StationPathResponse> execute(UUID meetingId) {

        // 위도 경도 상 중간 지점 찾기
        List<DepartureListResponse> departurelists = meetingRepository.findDeparturesAndNicknameByMeetingId(meetingId);

        double midLat = 0.0;
        double midLon = 0.0;

        Set<String> sets = new HashSet<>();

        for (DepartureListResponse departurelist : departurelists) {
            sets.add(departurelist.getDeparture());
        }

        for (String set : sets) {
            StationCoordinate locations = findStationCoordinateUseCase.excute(set);

            midLat += locations.getLatitude();
            midLon += locations.getLongitude();
        }

        final double finalMidLat = midLat / sets.size();
        final double finalMidLon = midLon / sets.size();

        // 중간 지점과 가까운 순으로 5개 번화가 추출
        List<HotPlaceCategoryResponse> fivehotlists = hotPlaceRepository.findAll().stream()
                .map(HotPlaceCategoryResponse::from)
                .sorted((h1, h2) -> {
                    double d1 = calculateDistance(finalMidLat, finalMidLon, h1.getLatitude(), h1.getLongitude());
                    double d2 = calculateDistance(finalMidLat, finalMidLon, h2.getLatitude(), h2.getLongitude());
                    return Double.compare(d1, d2);
                })
                .limit(5)
                .toList();

        // 편차가 작은 번화가 3개 추출
        Map<List<SubwayRouteInfo>, Integer> resultWithDeviation = new HashMap<>();

        for (HotPlaceCategoryResponse fivehotlist : fivehotlists) {

            List<SubwayRouteInfo> routes = new ArrayList<>();

            for (DepartureListResponse departurelist : departurelists) {
                SubwayRouteInfo route =
                        subwayRouteService.getRoute(departurelist.getDeparture(), fivehotlist.getName());
                routes.add(route);
            }

            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            for (SubwayRouteInfo route : routes) {
                int time = route.getTotalTravelTime();
                min = Math.min(min, time);
                max = Math.max(max, time);
            }

            int deviation = max - min;

            // JSON 리스트 자체를 key로 저장
            resultWithDeviation.put(routes, deviation);
        }

        List<List<SubwayRouteInfo>> midlist = resultWithDeviation.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        List<StationPathResponse> stationPathResponses = midlist.stream() // List<List<Route>>
                .flatMap(List::stream) // Route 리스트를 풀어서 Stream<Route>
                .flatMap(route -> route.getStations().stream() // 각 Route의 stations 풀기
                        .map(station -> StationPathResponse.from(
                                station.getLineNumber(),  // lineNumber
                                station.getStationName(), // stationName
                                findStationCoordinateUseCase.excute(station.getStationName()).getLatitude(), // 위도 계산 메서드
                                findStationCoordinateUseCase.excute(station.getStationName()).getLongitude() // 경도 계산 메서드
                        ))
                )
                .collect(Collectors.toList());

        return stationPathResponses;

    }

    // 2개 지점의 거리 구하기
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // km 단위
    }

}
