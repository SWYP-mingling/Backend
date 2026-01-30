package swyp.mingling.domain.meeting.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 장소 추천 응답 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@Schema(description = "장소 추천 응답")
public class RecommendResponse {

    @Schema(description = "추천 장소 목록")
    private final List<PlaceInfo> placeInfos;

    @Schema(description = "무한 스크롤 페이징 정보")
    private final SliceInfo sliceInfo;

    /**
     * 단일 장소 정보
     */
    @Getter
    @Builder
    @Schema(description = "추천 장소 정보")
    public static class PlaceInfo {

        @Schema(description = "장소명", example = "투썸플레이스 합정역점")
        private final String placeName;

        @Schema(description = "상세 카테고리", example = "음식점 > 카페 > 커피전문점 > 투썸플레이스")
        private final String categoryName;

        @Schema(description = "카테고리 그룹명", example = "카페")
        private final String categoryGroupName;

        @Schema(description = "전화번호", example = "02-323-0026")
        private final String phone;

        @Schema(description = "지번 주소", example = "서울 마포구 합정동 414-3")
        private final String addressName;

        @Schema(description = "도로명 주소", example = "서울 마포구 독막로 5")
        private final String roadAddressName;

        @Schema(description = "카카오 장소 URL", example = "http://place.map.kakao.com/2049742")
        private final String placeUrl;

        @Schema(description = "경도(Longitude)", example = "126.91434766375866")
        private final String x;

        @Schema(description = "위도(Latitude)", example = "37.54894721263549")
        private final String y;
    }

    /**
     * 무한 스크롤 메타 정보
     */
    @Getter
    @Builder
    @Schema(description = "무한 스크롤 정보")
    public static class SliceInfo {

        @Schema(description = "다음 페이지 존재 여부")
        private final boolean hasNext;

        @Schema(description = "현재 페이지")
        private final int page;

        @Schema(description = "조회된 개수")
        private final int size;
    }
}