package swyp.mingling.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API 에러 코드 정의
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "요청 값이 유효하지 않습니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),

    // 409 CONFLICT
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "요청이 현재 리소스 상태와 충돌합니다."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."),

    // 모임 관련 에러
    INVALID_CAPACITY(HttpStatus.BAD_REQUEST, "INVALID_CAPACITY", "모임 인원은 최소 2명 이상이어야 합니다."),
    CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "CAPACITY_EXCEEDED", "해당 모임은 정원이 모두 찼습니다."),
    INVALID_DEADLINE(HttpStatus.BAD_REQUEST, "INVALID_DEADLINE", "마감 시간은 현재 시간 이후여야 합니다."),
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "MEETING_NOT_FOUND", "모임을 찾을 수 없습니다."),
    MEETING_CLOSED(HttpStatus.CONFLICT, "MEETING_CLOSED", "이미 마감된 모임입니다."),
    PURPOSE_NOT_FOUND(HttpStatus.BAD_REQUEST, "PURPOSE_NOT_FOUND", "일부 모임 목적을 찾을 수 없습니다."),
    HOT_PLACE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "HOT_PLACE_NOT_FOUND", "핫플레이스 정보를 찾을 수 없습니다."),
    DEPARTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "DEPARTURE_NOT_FOUND", "사용자의 출발지가 등록되지 않았습니다"),

    // 사용자 관련 에러
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "USER_UNAUTHORIZED", "사용자 인증에 실패했습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "이름이 이미 존재하거나 비밀번호가 틀렸습니다."),
    SESSION_COOKIE_EXPIRED(HttpStatus.UNAUTHORIZED, "SESSION_COOKIE_EXPIRED", "세션 및 쿠키가 만료가 되었습니다."),

    // 지하철 관련 에러
    STATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "STATION_NOT_FOUND", "유효하지 않은 역 이름입니다."),
    SUBWAY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "SUBWAY_ROUTE_NOT_FOUND", "해당하는 지하철 경로 데이터가 없습니다."),
    SUBWAY_API_INVALID_KEY(HttpStatus.UNAUTHORIZED, "SUBWAY_API_INVALID_KEY", "서울시 지하철 API 인증키가 유효하지 않습니다."),
    SUBWAY_API_MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "SUBWAY_API_MISSING_PARAMETER", "필수 요청 값이 누락되었습니다."),
    SUBWAY_API_SERVICE_NOT_FOUND(HttpStatus.BAD_REQUEST, "SUBWAY_API_SERVICE_NOT_FOUND", "해당하는 서비스를 찾을 수 없습니다."),
    SUBWAY_API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SUBWAY_API_SERVER_ERROR", "서울시 지하철 API 서버 오류가 발생했습니다."),
    SUBWAY_API_DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SUBWAY_API_DB_ERROR", "서울시 지하철 API 데이터베이스 연결 오류가 발생했습니다."),

    // 카카오 관련 에러
    INVALID_KAKAO_CATEGORY(HttpStatus.BAD_REQUEST,"INVALID_KAKAO_CATEGORY", "지원하지 않는 카카오 카테고리입니다."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL_API_ERROR", "외부 API 호출 중 오류가 발생했습니다.");
    /**
     * 상태 코드
     */
    private final HttpStatus status;

    /**
     * 에러 코드
     */
    private final String code;

    /**
     * 에러 메시지
     */
    private final String message;
}
