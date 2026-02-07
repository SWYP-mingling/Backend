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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MidPointAsyncUseCase {

    private final MeetingRepository meetingRepository;
    private final FindStationCoordinateUseCase findStationCoordinateUseCase;
    private final HotPlaceRepository hotPlaceRepository;
    private final SubwayRouteService subwayRouteService;

    public List<GetMidPointResponse> execute(UUID meetingId) {
        long startTime = System.currentTimeMillis();
        log.info("=== [MidPointAsyncUseCase] 중간지점 찾기 시작 (비동기) - meetingId: {} ===", meetingId);

        // 좌표 캐시 - 동일한 역 이름에 대한 중복 조회 방지
        Map<String, StationCoordinate> stationCoordinateCache = new ConcurrentHashMap<>();

        // 위도 경도 상 중간 지점 찾기
        List<DepartureListResponse> departurelists = meetingRepository.findDeparturesAndNicknameByMeetingId(meetingId);

        double midLat = 0.0;
        double midLon = 0.0;

        Set<String> sets = new HashSet<>();

        for (DepartureListResponse departurelist : departurelists) {
            sets.add(departurelist.getDeparture());
        }

        for (String set : sets) {
            StationCoordinate locations = stationCoordinateCache.computeIfAbsent(
                    set,
                    stationName -> findStationCoordinateUseCase.excute(stationName)
            );

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

        // 편차가 작은 번화가 3개 추출 (비동기 병렬 처리)
        List<CompletableFuture<MidPointCandidate>> candidateFutures = fivehotlists.stream()
                .map(fivehotlist -> CompletableFuture.supplyAsync(() -> {

                    // 각 후보 장소에 대해 모든 참여자의 경로를 병렬로 조회
                    List<CompletableFuture<SubwayRouteInfo>> routeFutures = departurelists.stream() // 동시요청
                            .map(departurelist -> CompletableFuture.supplyAsync(() -> { // 별도 스레드

                                if(departurelist.getDeparture().equals(fivehotlist.getName())) {
                                    // 같은 장소면 직접 생성
                                    return SubwayRouteInfo.builder()
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
                                    return subwayRouteService.getRoute(
                                            departurelist.getDeparture(),
                                            fivehotlist.getName()
                                    );
                                }
                            }))
                            .toList();

                    // 모든 경로 조회 완료 대기
                    List<SubwayRouteInfo> routes = routeFutures.stream()
                            .map(CompletableFuture::join)
                            .toList();

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

                    return new MidPointCandidate(routes, deviation, avgTime);
                }))
                .toList(); // 객체를 하나의 리스트로 생성

        // 모든 후보지 처리 완료 대기
        List<MidPointCandidate> candidates = candidateFutures.stream()
                .map(CompletableFuture::join)
                .toList();

        List<List<SubwayRouteInfo>> midlist =
                candidates.stream()
                        .sorted(
                                Comparator.comparing(MidPointCandidate::getDeviation)
                                        .thenComparing(MidPointCandidate::getAvgTime)
                        )
                        .limit(3)
                        .map(MidPointCandidate::getRoutes)
                        .toList();

        // 1. 결과 데이터를 담을 리스트 (좌표 조회 캐시 적용)
        List<GetMidPointResponse> finalResult = midlist.stream()
                .map(routeList -> {
                    // 이 그룹의 공통 목적지 추출
                    String endStationName = routeList.get(0).getEndStation();
                    String endStationLine = routeList.get(0).getEndStationLine();

                    // 목적지 좌표 캐싱
                    StationCoordinate endStationCoord = stationCoordinateCache.computeIfAbsent( // 만약 데이터가 없으면 계산
                            endStationName,
                            stationName -> findStationCoordinateUseCase.excute(stationName)
                    );

                    // 2. 인덱스를 활용해 사용자별 닉네임과 경로 정보를 매핑 (IntStream 사용)
                    List<UserRouteDto> userRouteDtos = IntStream.range(0, routeList.size())
                            .mapToObj(i -> {
                                SubwayRouteInfo route = routeList.get(i);
                                // 기존 참여자 리스트(departurelists)에서 같은 순서의 닉네임을 가져옴
                                String nickname = departurelists.get(i).getNickname();

                                // 출발역 좌표 캐싱
                                StationCoordinate startStationCoord = stationCoordinateCache.computeIfAbsent(
                                        route.getStartStation(),
                                        stationName -> findStationCoordinateUseCase.excute(stationName)
                                );

                                // 환승 경로의 좌표들 캐싱
                                List<StationPathResponse> transferPathResponses = route.getTransferPath().stream()
                                        .map(transfer -> {
                                            StationCoordinate coord = stationCoordinateCache.computeIfAbsent(
                                                    transfer.getStationName(),
                                                    stationName -> findStationCoordinateUseCase.excute(stationName)
                                            );
                                            return StationPathResponse.from(
                                                    transfer.getLineName(),
                                                    transfer.getStationName(),
                                                    coord.getLatitude(),
                                                    coord.getLongitude()
                                            );
                                        })
                                        .toList();

                                // 경로 상 역들의 좌표 캐싱
                                List<StationPathResponse> stationResponses = route.getStations().stream()
                                        .map(station -> {
                                            StationCoordinate coord = stationCoordinateCache.computeIfAbsent(
                                                    station.getStationName(),
                                                    stationName -> findStationCoordinateUseCase.excute(stationName)
                                            );
                                            return StationPathResponse.from(
                                                    station.getLineNumber(),
                                                    station.getStationName(),
                                                    coord.getLatitude(),
                                                    coord.getLongitude()
                                            );
                                        })
                                        .toList();

                                return UserRouteDto.builder()
                                        .nickname(nickname)
                                        .startStation(route.getStartStation())
                                        .startStationLine(route.getStartStationLine())
                                        .latitude(startStationCoord.getLatitude())
                                        .longitude(startStationCoord.getLongitude())
                                        .travelTime(route.getTotalTravelTime())
                                        .transferPath(transferPathResponses)
                                        .stations(stationResponses)
                                        .build();
                            })
                            .toList();

                    // 3. 최종 추천 장소 객체 생성
                    return GetMidPointResponse.builder()
                            .endStationLine(endStationLine)
                            .endStation(endStationName)
                            .latitude(endStationCoord.getLatitude())
                            .longitude(endStationCoord.getLongitude())
                            .userRoutes(userRouteDtos)
                            .build();
                })
                .toList();

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("Total station coordinate cache hits: {}", stationCoordinateCache.size());
        log.info("=== [MidPointAsyncUseCase] 중간지점 찾기 완료 (비동기) - meetingId: {}, 걸린 시간: {}ms ({} 초) ===",
                 meetingId, elapsedTime, elapsedTime / 1000.0);
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
