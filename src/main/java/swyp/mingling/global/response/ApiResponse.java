package swyp.mingling.global.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import swyp.mingling.global.exception.ErrorCode;

/**
 * API 응답 공통 형식
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * 응답 성공 여부 (필수)
     */
    private final boolean success;

    /**
     * 응답/에러 코드 (선택)
     */
    private final String code;

    /**
     * 응답/에러 메시지 (선택)
     */
    private final String message;

    /**
     * 실제 응답 데이터 (선택)
     */
    private final T data;

    /**
     * 응답 시간 (필수, KST 기준 ISO 8601 형식)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime timestamp;

    /**
     * 성공 응답 생성 (최소 구조: success, data, timestamp)
     *
     * @param data 응답 데이터
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, null, data, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }

    /**
     * 성공 응답 생성 (데이터 없음)
     *
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, null, null, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }

    /**
     * 성공 응답 생성 (code, message 포함)
     * 필요에 따라 code와 message를 포함할 때 사용
     *
     * @param code 응답 코드
     * @param message 응답 메시지
     * @param data 응답 데이터
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return new ApiResponse<>(true, code, message, data, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }

    /**
     * 실패 응답 생성
     *
     * @param code 에러 코드
     * @param message 에러 메시지
     * @return 실패 응답 객체
     */
    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, code, message, null, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }

    /**
     * 실패 응답 생성 (ErrorCode 사용)
     *
     * @param errorCode 에러 코드 enum
     * @return 실패 응답 객체
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }

    /**
     * 실패 응답 생성 (상세 데이터 포함)
     * Validation 에러 상세 등을 보낼 때 사용
     */
    public static <T> ApiResponse<T> error(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }
}
