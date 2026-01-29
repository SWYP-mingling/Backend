package swyp.mingling.global.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ParticipantApiDocumentation {

    /**
     * 출발지 수정 API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "출발지 수정 API",
            description = "로그인된 사용자의 세션(nickname)을 기반으로 특정 모임(UUID로 구분)에 참여 중인 사용자의 출발지를 수정하고 사용자 정보와 출발지를 반환합니다."
    )
    @ApiResponses({
            // SUCCESS
            @ApiResponse(
                    responseCode = "200",
                    description = "출발지 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "SUCCESS",
                                    description = "출발지 수정 성공",
                                    value = """
                    {
                      "success": true,
                      "data": {
                        "userName": "홍길동",
                        "departureName": "합정역",
                        "latitude": 37.497942,
                        "longitude": 127.027621
                      },
                      "timestamp": "2026-01-19T21:30:00"
                    }
                    """
                            )
                    )
            ),

            // BAD_REQUEST
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "BAD_REQUEST",
                                            description = "잘못된 요청",
                                            value = """
                        {
                          "success": false,
                          "code": "BAD_REQUEST",
                          "message": "잘못된 요청입니다.",
                          "timestamp": "2026-01-19T21:30:00"
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "VALIDATION_ERROR",
                                            description = "유효성 검사 실패",
                                            value = """
                        {
                          "success": false,
                          "code": "VALIDATION_ERROR",
                          "message": "출발지는 필수입니다.",
                          "timestamp": "2026-01-19T21:30:00"
                        }
                        """
                                    )
                            }
                    )
            ),

            // INTERNAL_SERVER_ERROR
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "INTERNAL_SERVER_ERROR",
                  "message": "서버 내부 오류가 발생했습니다.",
                  "timestamp": "2026-01-19T21:30:00"
                }
                """)
                    )
            )
    })
    public @interface UpdateDepartDoc {}

    /**
     * 출발역 등록 API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "출발역 등록 API",
        description = "로그인된 사용자의 세션(nickname)을 기반으로 모임 참여자의 출발역 정보를 등록합니다."
    )
    @ApiResponses({
        // SUCCESS
        @ApiResponse(
            responseCode = "200",
            description = "출발역 등록 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                examples = @ExampleObject(
                    name = "SUCCESS",
                    value = """
                {
                  "success": true,
                  "data": {
                    "userName": "김밍글",
                    "stationName": "구로디지털단지역",
                    "latitude": 37.485266,
                    "longitude": 126.901401
                  },
                  "timestamp": "2026-01-24T04:00:00"
                }
                """
                )
            )
        ),

        // STATION_NOT_FOUND
        @ApiResponse(
            responseCode = "400",
            description = "유효하지 않은 역 이름",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "STATION_NOT_FOUND",
                    value = """
                {
                  "success": false,
                  "code": "STATION_NOT_FOUND",
                  "message": "유효하지 않은 역 이름입니다.",
                  "timestamp": "2026-01-24T04:00:00"
                }
                """
                )
            )
        ),

        // USER_UNAUTHORIZED
        @ApiResponse(
            responseCode = "401",
            description = "사용자 인증 실패",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "USER_UNAUTHORIZED",
                    value = """
                {
                  "success": false,
                  "code": "USER_UNAUTHORIZED",
                  "message": "사용자 인증에 실패했습니다.",
                  "timestamp": "2026-01-24T04:00:00"
                }
                """
                )
            )
        ),

        // MEETING_NOT_FOUND
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 모임",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "MEETING_NOT_FOUND",
                    value = """
                {
                  "success": false,
                  "code": "MEETING_NOT_FOUND",
                  "message": "모임을 찾을 수 없습니다.",
                  "timestamp": "2026-01-24T04:00:00"
                }
                """
                )
            )
        ),

        // MEETING_CLOSED
        @ApiResponse(
            responseCode = "409",
            description = "마감된 모임",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "MEETING_CLOSED",
                    value = """
                {
                  "success": false,
                  "code": "MEETING_CLOSED",
                  "message": "이미 마감된 모임입니다.",
                  "timestamp": "2026-01-24T04:00:00"
                }
                """
                )
            )
        ),

        // INTERNAL_SERVER_ERROR
        @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "INTERNAL_SERVER_ERROR",
                    value = """
                {
                  "success": false,
                  "code": "INTERNAL_SERVER_ERROR",
                  "message": "서버 내부 오류가 발생했습니다.",
                  "timestamp": "2026-01-24T04:00:00"
                }
                """
                )
            )
        )
    })
    public @interface CreateDepartureDoc {}


    /**
     * 모임 입장하기 API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "모임 입장하기 API",
            description = "모임 UUID와 이름, 비밀번호를 입력하여 모임에 입장합니다. " +
                    "기존 참여자인 경우 비밀번호를 검증하여 로그인하고, " +
                    "신규 참여자인 경우 자동으로 등록합니다. " +
                    "성공 시 JWT 토큰을 발급합니다."
    )
    @ApiResponses({
            // SUCCESS
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 입장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "SUCCESS",
                                    value = """
                    {
                      "success": true,
                      "timestamp": "2026-01-21T20:30:00"
                    }
                    """
                            )
                    )
            ),

            // MEETING_NOT_FOUND
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "MEETING_NOT_FOUND",
                                    description = "필수 값 누락 또는 유효성 검사 실패",
                                    value = """
                {
                  "success": false,
                  "code": "MEETING_NOT_FOUND",
                  "message": "모임을 찾을 수 없습니다.",
                  "timestamp": "2026-01-24T04:00:00"
                }
                """
                            )
                    )
            ),

            // USER_UNAUTHORIZED
            @ApiResponse(
                    responseCode = "401",
                    description = "세션 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "USER_UNAUTHORIZED",
                                    value = """
                {
                  "success": false,
                  "code": "USER_UNAUTHORIZED",
                  "message": "사용자 인증에 실패했습니다.",
                  "timestamp": "2026-01-24T04:00:00"
                }
                """
                            )
                    )
            ),

            // BAD_REQUEST
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "BAD_REQUEST",
                                            description = "잘못된 요청",
                                            value = """
                        {
                          "success": false,
                          "code": "BAD_REQUEST",
                          "message": "잘못된 요청입니다.",
                          "timestamp": "2026-01-19T21:30:00"
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "VALIDATION_ERROR",
                                            description = "유효성 검사 실패",
                                            value = """
                        {
                          "success": false,
                          "code": "VALIDATION_ERROR",
                          "message": "출발지는 필수입니다.",
                          "timestamp": "2026-01-19T21:30:00"
                        }
                        """
                                    )
                            }
                    )
            ),

            // INTERNAL_SERVER_ERROR
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "INTERNAL_SERVER_ERROR",
                                    value = """
                {
                  "success": false,
                  "code": "INTERNAL_SERVER_ERROR",
                  "message": "서버 내부 오류가 발생했습니다.",
                  "timestamp": "2026-01-24T04:00:00"
                }
                """
                            )
                    )
            )
    })
    public @interface EnterMeetingDoc {}

}
