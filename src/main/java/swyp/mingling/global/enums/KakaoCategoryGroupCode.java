package swyp.mingling.global.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swyp.mingling.global.exception.BusinessException;

/**
 * 카카오 API에서 제공하는 카테고리 그룹 코드 Enum
 * 출처: https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword-request-query-category-group-code
 */
@Getter
@RequiredArgsConstructor
public enum KakaoCategoryGroupCode {

    MART("MT1", "대형마트"),
    CONVENIENCE_STORE("CS2", "편의점"),
    KINDERGARTEN("PS3", "어린이집, 유치원"),
    SCHOOL("SC4", "학교"),
    ACADEMY("AC5", "학원"),
    PARKING("PK6", "주차장"),
    GAS_STATION("OL7", "주유소, 충전소"),
    SUBWAY("SW8", "지하철역"),
    BANK("BK9", "은행"),
    CULTURE("CT1", "문화시설"),
    REAL_ESTATE("AG2", "중개업소"),
    PUBLIC_INSTITUTION("PO3", "공공기관"),
    ATTRACTION("AT4", "관광명소"),
    ACCOMMODATION("AD5", "숙박"),
    RESTAURANT("FD6", "음식점"),
    CAFE("CE7", "카페"),
    HOSPITAL("HP8", "병원"),
    PHARMACY("PM9", "약국");

    /**
     * 이름
     */
    private final String code;

    /**
     * 설명
     */
    private final String description;

    /**
     * 코드 값으로 Enum 조회
     *
     * @param code code 카카오 장소 카테고리 코드
     * @return 매칭되는 {@link KakaoCategoryGroupCode}
     */
    public static KakaoCategoryGroupCode fromCode(String code) {
        return Arrays.stream(values())
            .filter(c -> c.code.equals(code))
            .findFirst()
            .orElseThrow(BusinessException::invalidKakaoCategory);
    }

    /**
     * 카테고리 설명(한글명)으로 Enum 조회
     *
     * @param description 카카오 장소 카테고리 설명 (예: "카페", "음식점")
     * @return 매칭되는 {@link KakaoCategoryGroupCode}
     */
    public static KakaoCategoryGroupCode fromDescription(String description) {
        return Arrays.stream(values())
            .filter(c -> c.getDescription().equals(description))
            .findFirst()
            .orElseThrow(BusinessException::invalidKakaoCategory);
    }
}
