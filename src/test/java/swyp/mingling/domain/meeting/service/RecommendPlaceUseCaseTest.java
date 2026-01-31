package swyp.mingling.domain.meeting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import swyp.mingling.domain.meeting.dto.response.RecommendResponse;
import swyp.mingling.external.KakaoPlaceClient;
import swyp.mingling.external.dto.response.KakaoPlaceSearchResponse;
import swyp.mingling.external.dto.response.KakaoPlaceSearchResponse.Document;
import swyp.mingling.external.dto.response.KakaoPlaceSearchResponse.Meta;

@ExtendWith(MockitoExtension.class)
class RecommendPlaceUseCaseTest {

    @InjectMocks
    private RecommendPlaceUseCase recommendPlaceUseCase;

    @Mock
    private KakaoPlaceClient kakaoPlaceClient;

    /**
     * [성공 케이스]
     */
    @Test
    @DisplayName("장소 추천 목록을 정상적으로 조회한다.")
    public void success() {

        // given
        String midPlace = "합정역";
        String category = "카페";
        int page = 1;
        int size = 10;

        Document document = mockDocument(
            "두쫀쿠하우스",
            "카페",
            "음식점 > 카페",
            "02-1234-5678",
            "서울 마포구 합정동",
            "서울 마포구 양화로 1",
            "http://place.map.kakao.com/123",
            "126.1",
            "37.5"
        );

        Meta meta = new Meta(false, 10, 20);

        KakaoPlaceSearchResponse kakaoResponse = new KakaoPlaceSearchResponse(List.of(document),
            meta);

        when(kakaoPlaceClient.search(
            eq(midPlace),
            any(),
            eq(page),
            eq(size)
        )).thenReturn(kakaoResponse);

        // when
        RecommendResponse response = recommendPlaceUseCase.execute(midPlace, category, page, size);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getPlaceInfos()).hasSize(1);

        RecommendResponse.PlaceInfo placeInfo =
            response.getPlaceInfos().get(0);

        assertThat(placeInfo.getPlaceName()).isEqualTo("두쫀쿠하우스");
        assertThat(placeInfo.getCategoryGroupName()).isEqualTo("카페");
        assertThat(placeInfo.getCategoryName()).isEqualTo("음식점 > 카페");
        assertThat(placeInfo.getPhone()).isEqualTo("02-1234-5678");

        assertThat(response.getSliceInfo().isHasNext()).isTrue();
        assertThat(response.getSliceInfo().getPage()).isEqualTo(page);
        assertThat(response.getSliceInfo().getSize()).isEqualTo(1);

    }

    /**
     * [성공 케이스]
     */
    @Test
    @DisplayName("추천 장소가 없으면 빈 목록을 반환한다.")
    void success_emptyResult() {

        // given
        String midPlace = "합정역";
        String category = "카페";
        int page = 1;
        int size = 10;

        KakaoPlaceSearchResponse kakaoResponse =
            new KakaoPlaceSearchResponse(List.of(), new Meta(true, 0, 0));

        when(kakaoPlaceClient.search(
            eq(midPlace),
            any(),
            eq(page),
            eq(size)
        )).thenReturn(kakaoResponse);

        // when
        RecommendResponse response =
            recommendPlaceUseCase.execute(midPlace, category, page, size);

        // then
        assertThat(response.getPlaceInfos()).isEmpty();
        assertThat(response.getSliceInfo().isHasNext()).isFalse();
        assertThat(response.getSliceInfo().getSize()).isZero();
    }

    /**
     * [성공 케이스]
     */
    @Test
    @DisplayName("마지막 페이지에서는 다음 페이지가 없음을 반환한다.")
    void success_lastPage() {

        // given
        KakaoPlaceSearchResponse kakaoResponse =
            new KakaoPlaceSearchResponse(
                List.of(mockDocument("두쫀쿠하우스",
                    "카페",
                    "음식점 > 카페",
                    "02-1234-5678",
                    "서울 마포구 합정동",
                    "서울 마포구 양화로 1",
                    "http://place.map.kakao.com/123",
                    "126.1",
                    "37.5")),
                Meta.builder()
                    .isEnd(true)
                    .pageableCount(1)
                    .totalCount(1)
                    .build()
            );

        when(kakaoPlaceClient.search(any(), any(), anyInt(), anyInt()))
            .thenReturn(kakaoResponse);

        // when
        RecommendResponse response = recommendPlaceUseCase.execute("합정역", "카페", 3, 10);

        // then
        assertThat(response.getSliceInfo().isHasNext()).isFalse();
    }

    /**
     * 카카오 장소 검색 응답용 Document mock
     */
    private Document mockDocument(String placeName, String categoryGroupName, String categoryName,
        String phone,
        String addressName, String roadAddressName, String placeUrl, String x, String y) {
        Document doc = new Document().builder()
            .placeName(placeName)
            .categoryGroupName(categoryGroupName)
            .categoryName(categoryName)
            .phone(phone)
            .addressName(addressName)
            .roadAddressName(roadAddressName)
            .placeUrl(placeUrl)
            .x(x)
            .y(y)
            .build();
        return doc;
    }
}