package swyp.mingling.external;

import java.time.Duration;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import swyp.mingling.external.dto.response.KakaoPlaceSearchResponse;
import swyp.mingling.global.exception.BusinessException;

/**
 * 카카오 장소 검색 API 호출을 담당하는 Client 클래스
 */
@Slf4j
@Component
public class KakaoPlaceClient {

    private static final int API_TIMEOUT_SECONDS = 5;

    private final WebClient webClient;

    public KakaoPlaceClient(@Qualifier("kakaoWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * 키워드 장소 검색
     *
     * @param query 검색 키워드 (예: "합정역")
     * @param categoryGroupCode categoryGroupCode 카카오 카테고리 그룹 코드
     * @param page 조회할 페이지 번호 (1부터 시작)
     * @param size 조회할 개수
     * @return 카카오 장소 검색 응답
     */
    public KakaoPlaceSearchResponse search(String query, String categoryGroupCode, int page, int size) {

        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/v2/local/search/keyword.json")
                    .queryParam("query", query)
                    .queryParamIfPresent("category_group_code", Optional.ofNullable(categoryGroupCode))
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
                )
                .retrieve()
                .bodyToMono(KakaoPlaceSearchResponse.class)
                .timeout(Duration.ofSeconds(API_TIMEOUT_SECONDS))
                .block();
        } catch (Exception e) {
            log.error("Kakao place search API call failed. query: {}, category: {}, page: {}, size: {}",
                query, categoryGroupCode, page, size, e);
            throw BusinessException.externalApiError();
        }

    }
}
