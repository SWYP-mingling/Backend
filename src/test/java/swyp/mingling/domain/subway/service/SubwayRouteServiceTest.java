package swyp.mingling.domain.subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import swyp.mingling.domain.subway.dto.SubwayRouteInfo;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 서울시 지하철 API 실제 호출 통합 테스트
 *
 * 주의: 이 테스트는 실제 서울시 공공 API를 호출.
 * - API 키 환경변수 설정 필요 (SEOUL_METRO_API_KEY)
 * - API 호출 제한에 유의
 */
@TestPropertySource(properties = {
       "seoul.metro.api-key=4a6342524f7373793633547a456576"
})
@SpringBootTest(classes = {
        SubwayRouteService.class,
        swyp.mingling.external.SeoulMetroClient.class,
        swyp.mingling.domain.subway.parser.SeoulMetroRouteParser.class,
        swyp.mingling.global.config.SeoulMetroWebClientConfig.class
})
@ActiveProfiles("test")
class SubwayRouteServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SubwayRouteServiceTest.class);

    @Autowired
    private SubwayRouteService subwayRouteService;

    @Test
    @DisplayName("실제 API 호출 - 출발역-도착역 경로 조회")
    void testGetRoute_RealAPI() {
        // Given
        String startStation = "강남역";
        String endStation = "수원역";

        // When
        SubwayRouteInfo routeInfo = subwayRouteService.getRoute(startStation, endStation);

        // Then
        assertThat(routeInfo).isNotNull();
        assertThat(routeInfo.getStartStation()).isNotEmpty();
        assertThat(routeInfo.getStartStationLine()).isNotEmpty();
        assertThat(routeInfo.getEndStation()).isNotEmpty();
        assertThat(routeInfo.getEndStationLine()).isNotEmpty();
        assertThat(routeInfo.getTotalTravelTime()).isGreaterThan(0);
        assertThat(routeInfo.getTotalDistance()).isGreaterThan(0.0);
        assertThat(routeInfo.getTransferCount()).isNotNull();
        assertThat(routeInfo.getTransferCount()).isGreaterThanOrEqualTo(0);
        assertThat(routeInfo.getTransferPath()).isNotNull();
        assertThat(routeInfo.getStations()).isNotEmpty();
        assertThat(routeInfo.getStationNames()).isNotEmpty();

        // 결과 출력
        log.info("====== {} → {} 경로 정보 ======", startStation, endStation);
        log.info("출발역: {} ({})", routeInfo.getStartStation(), routeInfo.getStartStationLine());
        log.info("도착역: {} ({})", routeInfo.getEndStation(), routeInfo.getEndStationLine());
        log.info("소요시간: {}분", routeInfo.getTotalTravelTime());
        log.info("이동거리: {}km", routeInfo.getTotalDistance());
        log.info("환승횟수: {}회", routeInfo.getTransferCount());

        // 환승 정보 출력
        log.info("====== 환승 정보 ======");
        if (routeInfo.getTransferPath().isEmpty()) {
            log.info("환승 없음 (직통)");
        } else {
            routeInfo.getTransferPath().forEach(transfer ->
                log.info("환승역: {}, 호선: {}", transfer.getStationName(), transfer.getLineName())
            );
        }

        // 경유 역 리스트 출력
        log.info("====== 경유 역 리스트 ======");
        log.info("총 경유 역 수: {}", routeInfo.getStations().size());

        // 경유 역 이름만 리스트로 출력
        log.info("경유 역 이름 리스트: {}", routeInfo.getStationNames());
        log.info("====================================");
    }
}
