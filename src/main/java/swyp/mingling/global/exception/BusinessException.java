package swyp.mingling.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 *
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

}
