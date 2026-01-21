package swyp.mingling.global.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

public class SampleApiDocumentation {

    /**
     * 샘플 GET API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "샘플 GET API",
        description = "Query Parameter 예시"
    )
    @ApiResponses({
        // SUCCESS
        @ApiResponse(
            responseCode = "200",
            description = "성공 예시",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject("""
                {
                  "success": true,
                  "data": {
                    "userId": 1,
                    "name": "테스트 사용자"
                  },
                  "timestamp": "2026-01-19T19:30:00"
                }
                """)
            )
        ),

        // 400
        // BAD_REQUEST, VALIDATION_ERROR
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
                  "timestamp": "2026-01-19T19:30:00"
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
                  "message": "요청 값이 유효하지 않습니다.",
                  "timestamp": "2026-01-19T19:30:00"
                }
                """
                    )
                }
            )
        ),


        // NOT_FOUND
        @ApiResponse(
            responseCode = "404",
            description = "리소스 없음 (NOT_FOUND)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "NOT_FOUND",
                  "message": "요청한 리소스를 찾을 수 없습니다.",
                  "timestamp": "2026-01-19T19:30:00"
                }
                """)
            )
        )
    })
    public @interface GetSampleDoc {
        long userId() default 0;
    }

    /**
     * 샘플 POST API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "샘플 POST API",
        description = "RequestBody DTO 예시"
    )
    @ApiResponses({
        // SUCCESS
        @ApiResponse(
            responseCode = "200",
            description = "성공 예시",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                examples = @ExampleObject("""
                {
                  "success": true,
                  "data": {
                    "userId": 10,
                    "name": "홍길동"
                  },
                  "timestamp": "2026-01-19T19:30:00"
                }
                """)
            )
        ),

        // UNAUTHORIZED
        @ApiResponse(
            responseCode = "401",
            description = "인증 필요 (UNAUTHORIZED)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "UNAUTHORIZED",
                  "message": "인증이 필요합니다.",
                  "timestamp": "2026-01-19T19:30:00"
                }
                """)
            )
        ),

        // FORBIDDEN
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음 (FORBIDDEN)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "FORBIDDEN",
                  "message": "접근 권한이 없습니다.",
                  "timestamp": "2026-01-19T19:30:00"
                }
                """)
            )
        ),

        // CONFLICT
        @ApiResponse(
            responseCode = "409",
            description = "상태 충돌 (CONFLICT)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "CONFLICT",
                  "message": "요청이 현재 리소스 상태와 충돌합니다.",
                  "timestamp": "2026-01-19T19:30:00"
                }
                """)
            )
        ),

        // INTERNAL_SERVER_ERROR
        @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류 (INTERNAL_SERVER_ERROR)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "INTERNAL_SERVER_ERROR",
                  "message": "서버 내부 오류가 발생했습니다.",
                  "timestamp": "2026-01-19T19:30:00"
                }
                """)
            )
        )
    })
    public @interface PostSampleDoc {}
}
