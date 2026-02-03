package swyp.mingling.global.exception;

import lombok.Getter;

/**
 * 비즈니스 로직에서 발생하는 공통 예외
 * 서비스 계층에서 발생하는 비즈니스 규칙 위반 시 사용
 */
@Getter
public class BusinessException extends RuntimeException{

    /**
     * 에러 코드
     */
    private final ErrorCode errorCode;

    /**
     * ErrorCode 와 HTTP 상태 코드를 지정한 예외 생성
     *
     * @param errorCode 에러 코드
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 모임을 찾을 수 없는 경우 사용하는 정적 팩토리 메서드
     *
     * @return 모임을 찾을 수 없음 예외
     */
    public static BusinessException meetingNotFound() {
        return new BusinessException(ErrorCode.MEETING_NOT_FOUND);
    }

    /**
     * 사용자 인증에 실패한 경우 사용하는 정적 팩토리 메서드
     *
     * @return 사용자 인증 실패 예외
     */

    public static BusinessException userUnauthorized() {
        return new BusinessException(ErrorCode.USER_UNAUTHORIZED);
    }

    /**
     * 존재하지 않는 지하철역이 입력된 경우 사용하는 정적 팩토리 메서드
     *
     * @return 지하철역을 찾을 수 없음 예외
     */
    public static BusinessException stationNotFound() {
        return new BusinessException(ErrorCode.STATION_NOT_FOUND);
    }

    /**
     * 이미 마감된 모임인 경우 사용하는 정적 팩토리 메서드
     *
     * @return 마감된 모임 예외
     */
    public static BusinessException meetingClosed() {
        return new BusinessException(ErrorCode.MEETING_CLOSED);
    }

    public static BusinessException meetingUser() {
        return new BusinessException(ErrorCode.INVALID_CREDENTIALS);
    }

    public static BusinessException sessionerror() {
        return new BusinessException(ErrorCode.SESSION_COOKIE_EXPIRED);
    }

    /**
     * 모임 인원이 초과된 경우 사용하는 정적 팩토리 메서드
     *
     * @return 모임 인원 초과 예외
     */
    public static BusinessException capacityExceeded() {
        return new BusinessException(ErrorCode.CAPACITY_EXCEEDED);
    }

    /**
     * 지원하지 않는 카카오 장소 카테고리가 입력된 경우 사용하는 정적 팩토리 메서드
     *
     * @return 지원하지 않는 카카오 카테고리 예외
     */
    public static BusinessException invalidKakaoCategory() {
        return new BusinessException(ErrorCode.INVALID_KAKAO_CATEGORY);
    }

    /**
     * 외부 API 호출 중 오류가 발생한 경우 사용하는 정적 팩토리 메서드
     *
     * @return 외부 API 호출 오류 예외
     */
    public static BusinessException externalApiError() {
        return new BusinessException(ErrorCode.EXTERNAL_API_ERROR);
    }

    /**
     * 모임 목적 카테고리를 찾을 수 없는 경우 사용하는 정적 팩토리 메서드
     *
     * @return 모임 목적 카테고리 미존재 예외
     */
    public static BusinessException purposeNotFound() {
        return new BusinessException(ErrorCode.PURPOSE_NOT_FOUND);
    }
}
