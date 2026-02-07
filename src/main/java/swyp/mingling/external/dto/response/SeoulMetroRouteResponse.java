package swyp.mingling.external.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 서울시 지하철 최단경로 API 응답 DTO
 *
 * 서울 열린데이터광장 - 지하철역 최단경로 이동정보 API 응답
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeoulMetroRouteResponse {

    /**
     * API 응답 헤더
     */
    @JsonProperty("header")
    private Header header;

    /**
     * API 응답 본문
     */
    @JsonProperty("body")
    private Body body;

    /**
     * API 응답 헤더
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;
    }

    /**
     * API 응답 본문
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        /**
         * 검색 타입
         */
        @JsonProperty("searchType")
        private String searchType;

        /**
         * 총 거리 (m)
         */
        @JsonProperty("totalDstc")
        private Integer totalDstc;

        /**
         * 총 소요시간 (초)
         */
        @JsonProperty("totalreqHr")
        private Integer totalreqHr;

        /**
         * 총 카드 요금
         */
        @JsonProperty("totalCardCrg")
        private Integer totalCardCrg;

        /**
         * 환승 횟수
         */
        @JsonProperty("trsitNmtm")
        private Integer trsitNmtm;

        /**
         * 경로 정보 목록
         */
        @JsonProperty("paths")
        private List<PathInfo> paths;
    }

    /**
     * 경로 구간 정보
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PathInfo {
        /**
         * 출발역 정보
         */
        @JsonProperty("dptreStn")
        private StationInfo dptreStn;

        /**
         * 도착역 정보
         */
        @JsonProperty("arvlStn")
        private StationInfo arvlStn;

        /**
         * 구간 거리 (m)
         */
        @JsonProperty("stnSctnDstc")
        private Integer stnSctnDstc;

        /**
         * 구간 소요시간 (초)
         */
        @JsonProperty("reqHr")
        private Integer reqHr;

        /**
         * 대기시간 (초)
         */
        @JsonProperty("wtngHr")
        private Integer wtngHr;

        /**
         * 환승 여부 (Y/N)
         */
        @JsonProperty("trsitYn")
        private String trsitYn;
    }

    /**
     * 역 정보
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StationInfo {
        /**
         * 역 코드
         */
        @JsonProperty("stnCd")
        private String stnCd;

        /**
         * 역 번호
         */
        @JsonProperty("stnNo")
        private String stnNo;

        /**
         * 역 이름
         */
        @JsonProperty("stnNm")
        private String stnNm;

        /**
         * 호선명
         */
        @JsonProperty("lineNm")
        private String lineNm;
    }

    /**
     * API 호출 성공 여부 확인
     *
     * @return 성공 시 true
     */
    public boolean isSuccess() {
        return header != null && "00".equals(header.getResultCode());
    }

    /**
     * API 응답 에러 코드 반환
     *
     * @return 에러 코드 (헤더가 없으면 null)
     */
    public String getErrorCode() {
        return header != null ? header.getResultCode() : null;
    }

    /**
     * API 응답 에러 메시지 반환
     *
     * @return 에러 메시지 (헤더가 없으면 null)
     */
    public String getErrorMessage() {
        return header != null ? header.getResultMsg() : null;
    }

    /**
     * 데이터 없음 여부 확인 (INFO-200)
     *
     * @return 데이터 없음 시 true
     */
    public boolean isNoData() {
        return header != null && "INFO-200".equals(header.getResultCode());
    }

    /**
     * 인증키 오류 여부 확인 (INFO-100)
     *
     * @return 인증키 오류 시 true
     */
    public boolean isInvalidApiKey() {
        return header != null && "INFO-100".equals(header.getResultCode());
    }

    /**
     * 필수 값 누락 오류 여부 확인 (ERROR-300)
     *
     * @return 필수 값 누락 시 true
     */
    public boolean isMissingParameter() {
        return header != null && "ERROR-300".equals(header.getResultCode());
    }

    /**
     * 서비스 없음 오류 여부 확인 (ERROR-310)
     *
     * @return 서비스 없음 시 true
     */
    public boolean isServiceNotFound() {
        return header != null && "ERROR-310".equals(header.getResultCode());
    }

    /**
     * 서버 오류 여부 확인 (ERROR-500, ERROR-600, ERROR-601)
     *
     * @return 서버 오류 시 true
     */
    public boolean isServerError() {
        if (header == null) {
            return false;
        }
        String code = header.getResultCode();
        return "ERROR-500".equals(code) || "ERROR-600".equals(code) || "ERROR-601".equals(code);
    }

    /**
     * 경로 정보 목록 반환
     *
     * @return 경로 정보 리스트 (없으면 빈 리스트)
     */
    public List<PathInfo> getPathInfoList() {
        if (body == null || body.getPaths() == null) {
            return List.of();
        }
        return body.getPaths();
    }
}
