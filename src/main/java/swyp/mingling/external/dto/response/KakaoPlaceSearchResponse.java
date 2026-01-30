package swyp.mingling.external.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * 카카오 장소 검색 API 응답 DTO
 * documents : 검색된 장소 목록
 * meta      : 페이징 및 검색 메타 정보
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoPlaceSearchResponse {

    /**
     * 검색된 장소 목록
     */
    private List<Document> documents;

    /**
     * 검색 결과 메타 정보
     */
    private Meta meta;

    /**
     * 개별 장소 정보 DTO
     */
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Document {

        private String addressName;
        private String categoryGroupCode;
        private String categoryGroupName;
        private String categoryName;
        private String distance;
        private String phone;
        private String placeName;
        private String placeUrl;
        private String roadAddressName;
        private String x;
        private String y;
    }

    /**
     * 검색 결과 메타 정보
     */
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {

        @JsonProperty("is_end")
        private boolean isEnd;

        private int pageableCount;
        private int totalCount;
    }
}
