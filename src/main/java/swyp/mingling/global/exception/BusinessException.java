package swyp.mingling.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

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
}
