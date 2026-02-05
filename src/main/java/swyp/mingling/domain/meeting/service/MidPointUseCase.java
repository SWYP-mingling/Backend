package swyp.mingling.domain.meeting.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.mingling.domain.meeting.dto.response.midpoint.*;
import swyp.mingling.domain.meeting.dto.StationCoordinate;
import swyp.mingling.domain.meeting.repository.HotPlaceRepository;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.domain.subway.dto.SubwayRouteInfo;
import swyp.mingling.domain.subway.service.SubwayRouteService;

import java.util.*;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MidPointUseCase {

    private final MeetingRepository meetingRepository;
    private final FindStationCoordinateUseCase findStationCoordinateUseCase;
    private final HotPlaceRepository hotPlaceRepository;
    private final SubwayRouteService subwayRouteService;

    public List<GetMidPointResponse> execute(UUID meetingId) {

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
        List<MidPointCandidate> candidates = new ArrayList<>();

        for (HotPlaceCategoryResponse fivehotlist : fivehotlists) {

            List<SubwayRouteInfo> routes = new ArrayList<>();

            for (DepartureListResponse departurelist : departurelists) {

                SubwayRouteInfo route;

                // @@@@@@@@@@@@ 추가 확인 필요 @@@@@@@@@@@
                if(departurelist.getDeparture().equals(fivehotlist.getName())) {
                    // 같은 장소면 직접 생성
                    route = SubwayRouteInfo.builder()
                            .startStation(departurelist.getDeparture())
                            .startStationLine(fivehotlist.getLine())
                            .endStation(fivehotlist.getName())
                            .endStationLine(fivehotlist.getLine())
                            .totalTravelTime(0)
                            .transferCount(0)
                            .transferPath(List.of())
                            .stations(List.of())
                            .build();
                } else {
                    // 다르면 API 호출
                    route = subwayRouteService.getRoute(
                            departurelist.getDeparture(),
                            fivehotlist.getName()
                    );
                }
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

            int sum = 0;
            for (SubwayRouteInfo route : routes) {
                sum += route.getTotalTravelTime();
            }

            int avgTime = sum / routes.size();

            candidates.add(
                    new MidPointCandidate(routes, deviation, avgTime)
            );
        }

        List<List<SubwayRouteInfo>> midlist =
                candidates.stream()
                        .sorted(
                                Comparator.comparing(MidPointCandidate::getDeviation)
                                        .thenComparing(MidPointCandidate::getAvgTime)
                        )
                        .limit(3)
                        .map(MidPointCandidate::getRoutes)
                        .toList();

        // 1. 결과 데이터를 담을 리스트
        List<GetMidPointResponse> finalResult = midlist.stream()
                .map(routeList -> {
                    // 이 그룹의 공통 목적지 추출
                    String endStationName = routeList.get(0).getEndStation();
                    String endStationLine = routeList.get(0).getEndStationLine();

                    // 2. 인덱스를 활용해 사용자별 닉네임과 경로 정보를 매핑 (IntStream 사용)
                    List<UserRouteDto> userRouteDtos = IntStream.range(0, routeList.size())
                            .mapToObj(i -> {
                                SubwayRouteInfo route = routeList.get(i);
                                // 기존 참여자 리스트(departurelists)에서 같은 순서의 닉네임을 가져옴
                                String nickname = departurelists.get(i).getNickname();

                                return UserRouteDto.builder()
                                        .nickname(nickname)
                                        .startStation(route.getStartStation())
                                        .startStationLine(route.getStartStationLine())
                                        .latitude(findStationCoordinateUseCase.excute(route.getStartStation()).getLatitude())
                                        .longitude(findStationCoordinateUseCase.excute(route.getStartStation()).getLongitude())
                                        .travelTime(route.getTotalTravelTime())
                                        .transferPath(route.getTransferPath().stream().
                                                map(transfer -> StationPathResponse.from(
                                                        transfer.getLineName(),
                                                        transfer.getStationName(),
                                                        findStationCoordinateUseCase.excute(transfer.getStationName()).getLatitude(),
                                                        findStationCoordinateUseCase.excute(transfer.getStationName()).getLongitude())).toList())


                                        .stations(route.getStations().stream().
                                                map(station -> StationPathResponse.from(
                                                        station.getLineNumber(),
                                                        station.getStationName(),
                                                        findStationCoordinateUseCase.excute(station.getStationName()).getLatitude(),
                                                        findStationCoordinateUseCase.excute(station.getStationName()).getLongitude())).toList())
                                        .build();
                            })
                            .toList();

                    // 3. 최종 추천 장소 객체 생성
                    return GetMidPointResponse.builder()
                            .endStationLine(endStationLine)
                            .endStation(endStationName)
                            .latitude(findStationCoordinateUseCase.excute(endStationName).getLatitude())
                            .longitude(findStationCoordinateUseCase.excute(endStationName).getLongitude())
                            .userRoutes(userRouteDtos)
                            .build();
                })
                .toList();

        return finalResult;

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
