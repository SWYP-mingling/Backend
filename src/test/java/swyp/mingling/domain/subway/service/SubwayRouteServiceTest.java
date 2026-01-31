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
        "seoul.metro.api-key=지하철 API 키 입력",
})
@SpringBootTest
@ActiveProfiles("test")
class SubwayRouteServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SubwayRouteServiceTest.class);

    @Autowired
    private SubwayRouteService subwayRouteService;

    @Test
    @DisplayName("실제 API 호출 - 강남역에서 서울역까지 전체 경로 조회")
    void testGetRoute_RealAPI() {
        // Given
        String startStation = "강남역";
        String endStation = "마곡역";

        // When
        SubwayRouteInfo routeInfo = subwayRouteService.getRoute(startStation, endStation);

        // Then
        assertThat(routeInfo).isNotNull();
        assertThat(routeInfo.getTotalTravelTime()).isGreaterThan(0);
        assertThat(routeInfo.getTotalDistance()).isGreaterThan(0.0);
        assertThat(routeInfo.getTransferPath()).isNotEmpty();
        assertThat(routeInfo.getStations()).isNotEmpty();
        assertThat(routeInfo.getStationNames()).isNotEmpty();

        // 결과 출력
        log.info("====== 강남역 → 신사역 경로 정보 ======");
        log.info("출발역: {}", routeInfo.getStartStation());
        log.info("도착역: {}", routeInfo.getEndStation());
        log.info("소요시간: {}분", routeInfo.getTotalTravelTime());
        log.info("이동거리: {}km", routeInfo.getTotalDistance());
        log.info("환승경로: {}", routeInfo.getTransferPath());

        // 경유 역 리스트 출력
        log.info("====== 경유 역 리스트 ======");
        log.info("총 경유 역 수: {}", routeInfo.getStations().size());

        // 경유 역 이름만 리스트로 출력
        log.info("경유 역 이름 리스트: {}", routeInfo.getStationNames());
        log.info("====================================");
    }
}
