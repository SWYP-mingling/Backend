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

        // ========================================
        // 편차가 작은 번화가 3개 추출 (비동기 병렬 처리)
        // ========================================
        // [개선 이유]
        // 기존 방식: CompletableFuture.supplyAsync 안에서 루프로 join() 호출
        //   - 비동기처럼 보이지만 실제로는 순차 대기하여 성능 저하
        //   - API 응답 지연 시 서버가 강제로 연결 종류 위험성
        //   - 예시: 핫플1 처리 → 핫플2 처리 → 핫플3 처리 (순차)
        //
        // 개선 방식: allOf()로 모든 Future를 한번에 묶어 병렬 처리
        //   - 모든 API 호출이 동시에 시작되어 대기 시간 최소화
        //   - 타임아웃 위험 감소 및 전체 응답 속도 향상
        //   - 예시: 핫플1, 핫플2, 핫플3 모두 동시 처리 → 가장 느린 것만 대기
        // ========================================

        // 1단계: 모든 API 호출을 비동기로 먼저 시작
        List<CompletableFuture<MidPointCandidate>> candidateFutures = fivehotlists.stream()
                .map(fivehotlist -> {
                    // 각 후보 장소에 대해 모든 참여자의 경로를 병렬로 조회
                    List<CompletableFuture<SubwayRouteInfo>> routeFutures = departurelists.stream()
                            .map(departurelist -> CompletableFuture.supplyAsync(() -> {
                                // 역 이름 정규화 (비교 및 반환값 일관성을 위해 "역" 제거)
                                String normalizedDeparture = normalizeStationName(departurelist.getDeparture());
                                String normalizedHotPlace = normalizeStationName(fivehotlist.getName());

                                if(normalizedDeparture.equals(normalizedHotPlace)) {
                                    // 같은 장소면 직접 생성 (정규화된 이름 사용)
                                    return SubwayRouteInfo.builder()
                                            .startStation(normalizedDeparture)
                                            .startStationLine(fivehotlist.getLine())
                                            .endStation(normalizedHotPlace)
                                            .endStationLine(fivehotlist.getLine())
                                            .totalTravelTime(0)
                                            .transferCount(0)
                                            .transferPath(List.of())
                                            .stations(List.of())
                                            .build();
                                } else {
                                    // 다르면 API 호출 (API는 자체적으로 정규화된 값을 반환)
                                    return subwayRouteService.getRoute(
                                            departurelist.getDeparture(),
                                            fivehotlist.getName()
                                    );
                                }
                            }))
                            .toList();

                    // [개선사항]
                    // allOf()를 사용하여 모든 경로 조회를 한번에 대기
                    // 기존: routeFutures.stream().map(CompletableFuture::join) == 순차 대기
                    // 개선: allOf()로 묶어서 가장 느린 작업 하나만 기다림
                    CompletableFuture<Void> allRoutes = CompletableFuture.allOf(
                            routeFutures.toArray(new CompletableFuture[0])
                    );

                    // 모든 경로가 완료되면 MidPointCandidate 생성
                    // thenApply()로 비블로킹 방식으로 후속 처리
                    return allRoutes.thenApply(v -> {
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
                    });
                })
                .toList();

        // 2단계: 모든 후보지 처리가 완료될 때까지 한번에 대기
        // 5개 핫플레이스 × N명의 경로 조회가 모두 병렬로 실행
        // 가장 느린 API 호출이 완료될 때까지만 대기
        CompletableFuture<Void> allCandidates = CompletableFuture.allOf(
                candidateFutures.toArray(new CompletableFuture[0])
        );

        // 모든 작업 완료 대기 후 결과 수집
        allCandidates.join();

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

    /**
     * @param stationName 역 이름
     * @return 정규화된 역 이름 ("역" 제거)
     */
    private String normalizeStationName(String stationName) {
        if (stationName == null || stationName.isEmpty()) {
            return stationName;
        }

        // "역"이 끝에 붙어있으면 제거
        if (stationName.endsWith("역")) {
            return stationName.substring(0, stationName.length() - 1);
        }

        return stationName;
    }
}

