package swyp.mingling.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import swyp.mingling.global.response.ApiResponse;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 글로벌 예외 처리 핸들러
 * 모든 예외를 ApiResponse 형식으로 통일하여 응답
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 커스텀 비즈니스 예외 처리
     *
     * @param e 비즈니스 예외
     * @return 에러 응답
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("Business exception occurred: {}", e.getMessage());
        return ResponseEntity
            .status(e.getErrorCode().getStatus())
            .body(ApiResponse.error(e.getErrorCode()));
    }

    /**
     * 타입 변환 예외 처리
     *
     * @param e 타입 변환 예외
     * @return 에러 응답
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("Type mismatch exception occurred: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ErrorCode.BAD_REQUEST));
    }

    /**
     * IllegalArgumentException 처리
     *
     * @param e IllegalArgumentException
     * @return 에러 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument exception occurred: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ErrorCode.BAD_REQUEST.getCode(), e.getMessage()));
    }


    /**
     * MethodArgumentNotValidException 처리
     *
     * @param e MethodArgumentNotValidException
     * @return 에러 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> MethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        String message = errors.values().iterator().next();  // "비밀번호는 필수입니다."

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("BAD_REQUEST", message));
    }

    /**
     * 인증 예외 처리
     *
     * @param e 인증 예외
     * @return 에러 응답
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException e) {
        log.warn("Authentication exception occurred: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(ErrorCode.UNAUTHORIZED));
    }

    /**
     * 접근 권한 없음 예외 처리
     *
     * @param e 접근 권한 예외
     * @return 에러 응답
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("Access denied exception occurred: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error(ErrorCode.FORBIDDEN));
    }

    /**
     * 그 외 모든 예외 처리
     *
     * @param e 예외
     * @return 에러 응답
     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
//        log.error("Unexpected exception occurred", e);
//        return ResponseEntity
//            .status(HttpStatus.INTERNAL_SERVER_ERROR)
//            .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
//    }
}
