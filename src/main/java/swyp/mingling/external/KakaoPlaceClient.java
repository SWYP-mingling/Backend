package swyp.mingling.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import swyp.mingling.external.dto.response.KakaoPlaceSearchResponse;
import swyp.mingling.global.enums.KakaoCategoryGroupCode;

/**
 * 카카오 장소 검색 API 호출을 담당하는 Client 클래스
 */
@Component
@RequiredArgsConstructor
public class KakaoPlaceClient {

    private final WebClient webClient;

    /**
     * 키워드 장소 검색
     *
     * @param query 검색 키워드 (예: "합정역")
     * @param categoryGroupCode categoryGroupCode 카카오 카테고리 그룹 코드
     * @return 카카오 장소 검색 응답
     */
    public KakaoPlaceSearchResponse search(String query, KakaoCategoryGroupCode categoryGroupCode) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2/local/search/keyword.json")
                .queryParam("query", query)
                .queryParam("category_group_code", categoryGroupCode.getCode())
//                .queryParam("page", page)
//                .queryParam("size", size)
                .build()
            )
            .retrieve()
            .bodyToMono(KakaoPlaceSearchResponse.class)
            .block();
    }
}
