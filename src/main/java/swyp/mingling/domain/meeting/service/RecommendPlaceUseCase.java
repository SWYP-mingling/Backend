package swyp.mingling.domain.meeting.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swyp.mingling.domain.meeting.dto.response.RecommendResponse;
import swyp.mingling.external.KakaoPlaceClient;
import swyp.mingling.external.dto.response.KakaoPlaceSearchResponse;
import swyp.mingling.global.enums.KakaoCategoryGroupCode;

/**
 * 장소 추천 UseCase
 */
@Service
@RequiredArgsConstructor
public class RecommendPlaceUseCase {

    private final KakaoPlaceClient kakaoPlaceClient;

    /**
     * 장소 추천 목록 조회
     *
     * @param midPlace 중간 지점 키워드 (예: "합정역")
     * @param category 모임 목적 카테고리
     * @return 추천 장소 목록
     */
    public RecommendResponse execute(String midPlace, String category) {

        // 1. 카테고리 한글명을 카카오 카테고리 그룹 코드로 변환
        KakaoCategoryGroupCode categoryGroupCode = KakaoCategoryGroupCode.fromDescription(category);

        // 2. 카카오 장소 검색 API 호출
        KakaoPlaceSearchResponse response = kakaoPlaceClient.search(midPlace, categoryGroupCode);

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

        RecommendResponse.SliceInfo sliceInfo =
            RecommendResponse.SliceInfo.builder()
                .hasNext(!response.getMeta().isEnd())
//                .page(page)
//                .size(places.size())
                .build();

        return RecommendResponse.builder()
            .placeInfos(placeInfos)
            .sliceInfo(sliceInfo)
            .build();
    }
}