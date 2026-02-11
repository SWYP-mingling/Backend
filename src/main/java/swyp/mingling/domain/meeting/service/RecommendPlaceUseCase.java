package swyp.mingling.domain.meeting.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import swyp.mingling.domain.meeting.dto.response.RecommendResponse;
import swyp.mingling.external.KakaoPlaceClient;
import swyp.mingling.external.dto.response.KakaoPlaceSearchResponse;
import swyp.mingling.global.enums.KakaoCategoryGroupCode;
import swyp.mingling.global.exception.BusinessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 장소 추천 UseCase
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendPlaceUseCase {

    private final KakaoPlaceClient kakaoPlaceClient;

    /**
     * 장소 추천 목록 조회
     *
     * @param midPlace 중간 지점 키워드 (예: "합정역")
     * @param category 모임 목적 카테고리
     * @param page     조회할 페이지 번호 (1부터 시작)
     * @param size     조회할 개수 (기본값 15)
     * @return 추천 장소 목록
     */
    @Cacheable
    public RecommendResponse execute(String midPlace, String category, int page, int size) {

        log.info("[CACHE MISS] Call Kakao place search API - midPlace: {}, category: {}, page: {}, size: {}"
            , midPlace, category, page, size);

        // 1. 카테고리 한글명을 카카오 카테고리 그룹 코드로 변환
        Category categoryEnum = Category.from(category)
            .orElseThrow(BusinessException::purposeNotFound);

        // 2. 검색어 생성
        String query = buildSearchQuery(midPlace, categoryEnum);

        // 3. 카카오 장소 검색 API 호출
        KakaoPlaceSearchResponse response =
            kakaoPlaceClient.search(
                query,
                categoryEnum.getKakaoCode(),
                page,
                size
            );

        // 4. 장소 목록 변환
        List<RecommendResponse.PlaceInfo> placeInfos = response.getDocuments().stream()
            .map(doc -> RecommendResponse.PlaceInfo.builder()
                .placeName(doc.getPlaceName())
                .categoryName(doc.getCategoryName())
                .categoryGroupName(doc.getCategoryGroupName())
                .phone(doc.getPhone())
                .addressName(doc.getAddressName())
                .roadAddressName(doc.getRoadAddressName())
                .placeUrl(doc.getPlaceUrl())
                .x(doc.getX())
                .y(doc.getY())
                .build()
            )
            .toList();

        // 5. 페이징 메타 생성
        RecommendResponse.SliceInfo sliceInfo =
            RecommendResponse.SliceInfo.builder()
                .hasNext(!response.getMeta().isEnd())
                .page(page)
                .size(placeInfos.size())
                .build();

        return RecommendResponse.builder()
            .placeInfos(placeInfos)
            .sliceInfo(sliceInfo)
            .build();
    }

    /**
     * 중간 지점 키워드와 카테고리를 조합해 카카오 장소 검색에 사용할 검색어를 생성
     *
     * @param midPlace 중간 지점 키워드 (예: "합정역")
     * @param category 모임 목적 카테고리
     * @return 카카오 장소 검색에 사용할 검색 쿼리 문자열
     */
    private String buildSearchQuery(String midPlace, Category category) {
        return midPlace + " " + category.getQueryKeyword();
    }

    /**
     * 모임 목적 카테고리
     * 외부에서 전달된 한글 카테고리를 기준으로 카카오 장소 검색 API 에서 사용할 category_group_code 로 변환하기 위한 내부 ENUM
     */
    @Getter
    @RequiredArgsConstructor
    private enum Category {

        RESTAURANT("식당", "맛집", KakaoCategoryGroupCode.RESTAURANT.getCode()),
        CAFE("카페", "카페", KakaoCategoryGroupCode.CAFE.getCode()),
        BAR("술집", "술집", KakaoCategoryGroupCode.RESTAURANT.getCode()),
        STUDY_CAFE("스터디카페", "스터디카페", null),
        SPACE_RENTAL("장소대여", "모임공간", null),
        ENTERTAINMENT("놀거리", "실내데이트", null);

        private final String categoryName;
        private final String queryKeyword;
        private final String kakaoCode;

        /**
         * 외부에서 전달된 카테고리 문자열을 Category 를 enum 으로 변환
         *
         * @param category 외부 요청으로 전달된 카테고리 문자열
         * @return 일치하는 Optional<Category>
         */
        static Optional<Category> from(String category) {
            if (category == null || category.isBlank()) {
                return Optional.empty();
            }

            String categoryWithoutSpaces = category.replaceAll("\\s+", "");

            return Arrays.stream(values())
                .filter(c -> c.categoryName.equals(categoryWithoutSpaces))
                .findFirst();
        }
    }
}