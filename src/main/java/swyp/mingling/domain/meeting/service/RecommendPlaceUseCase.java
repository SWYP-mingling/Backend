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
    public List<RecommendResponse> execute(String midPlace, String category) {

        // 1. 카테고리 한글명을 카카오 카테고리 그룹 코드로 변환
        KakaoCategoryGroupCode categoryGroupCode = KakaoCategoryGroupCode.fromDescription(category);

        // 2. 카카오 장소 검색 API 호출
        KakaoPlaceSearchResponse response = kakaoPlaceClient.search(midPlace, categoryGroupCode);

        // 3. 카카오 응답을 API 응답 DTO로 변환
        return response.getDocuments().stream()
            .map(doc -> new RecommendResponse(
                doc.getPlaceName(),
                doc.getRoadAddressName()
            ))
            .toList();
    }
}