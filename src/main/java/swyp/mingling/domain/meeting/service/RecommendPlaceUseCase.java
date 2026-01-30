package swyp.mingling.domain.meeting.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import swyp.mingling.domain.meeting.dto.response.RecommendResponse;
import swyp.mingling.external.KakaoPlaceClient;
import swyp.mingling.external.dto.response.KakaoPlaceSearchResponse;
import swyp.mingling.global.enums.KakaoCategoryGroupCode;

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
     * @param size     조회할 개수 (기본값 10)
     * @return 추천 장소 목록
     */
    @Cacheable(
        cacheNames = "place-recommend",
        cacheManager = "placeCacheManager",
        key = "'recommend:' + #midPlace + ':' + #category + ':' + #page + ':' + #size"
    )
    public RecommendResponse execute(String midPlace, String category, int page, int size) {

        log.info("[CACHE MISS] Call Kakao place search API - midPlace: {}, category: {}, page: {}, size: {},"
            , midPlace, category, page, size);

        // 1. 카테고리 한글명을 카카오 카테고리 그룹 코드로 변환
        KakaoCategoryGroupCode categoryGroupCode = KakaoCategoryGroupCode.fromDescription(category);

        // 2. 카카오 장소 검색 API 호출
        KakaoPlaceSearchResponse response = kakaoPlaceClient.search(midPlace, categoryGroupCode, page, size);

        // 3. 장소 목록 변환
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

        // 4. 페이징 메타 생성
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
}