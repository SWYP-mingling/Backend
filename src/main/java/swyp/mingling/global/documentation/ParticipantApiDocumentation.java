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

            // BAD_REQUEST
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "VALIDATION_ERROR",
                                            description = "유효성 검사 실패",
                                            value = """
                        {
                          "success": false,
                          "code": "VALIDATION_ERROR",
                          "message": "출발지는 필수입니다. or 비밀번호는 필수입니다.",
                          "timestamp": "2026-01-19T21:30:00"
                        }
                        """
                                    )
                            }
                    )
            ),

            // SESSION_COOKIE_EXPIRED
            @ApiResponse(
                    responseCode = "401",
                    description = "세션 및 쿠키 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "SESSION_COOKIE_EXPIRED",
                                    description = "세션 및 쿠키 만료",
                                    value = """
                {
                  "success": false,
                  "code": "SESSION_COOKIE_EXPIRED",
                  "message": "세션 및 쿠키가 만료가 되었습니다.",
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
    public @interface EnterMeetingDoc {}

}
