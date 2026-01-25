package swyp.mingling.global.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

public class MeetingApiDocumentation {

    /**
     * 모임 생성 API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "모임 생성 API",
            description = "새로운 모임을 생성하고 모임 URL을 반환합니다."
    )
    @ApiResponses({
            // SUCCESS
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "SUCCESS",
                                    description = "모임 생성 성공",
                                    value = """
                    {
                      "success": true,
                      "data": {
                        "meetingUrl": "https://mingling.com/meeting/abc123def456"
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
                          "message": "모임명은 필수입니다.",
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
    public @interface CreateMeetingDoc {}

    /**
     * 중간지점 조회 API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "중간지점 조회 API",
            description = "참여자들의 출발지를 기준으로 중간지점(번화가) 최소 1개, 최대 3곳을 조회하고 정보 출력"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "중간지점 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                            examples =
                                    @ExampleObject(
                                            name = "응답",
                                            value = """
                                                        {
                                                          "success": true,
                                                          "data": {
                                                            "midpoints": [
                                                              {
                                                            "name": "합정역",
                                                            "latitude": 37.5484757,
                                                            "longitude": 126.912071,
                                                            "avgTravelTime": 30,
                                                            "transferPath": "버스 > 1호선 > 2호선"
                                                          },
                                                          {
                                                            "name": "서울역",
                                                            "latitude": 37.554648,
                                                            "longitude": 126.972559,
                                                            "avgTravelTime": 35,
                                                            "transferPath": "1호선 > 4호선"
                                                          },
                                                          {
                                                            "name": "용산역",
                                                            "latitude": 37.529844,
                                                            "longitude": 126.964804,
                                                            "avgTravelTime": 32,
                                                            "transferPath": "경의중앙선 > 1호선"
                                                          }
                                                        ]
                                                      },
                                                      "timestamp": "2026-01-21T15:15:00"
                                                    }
                                                    """
                                    )

                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 모임",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "NOT_FOUND",
                  "message": "모임을 찾을 수 없습니다.",
                  "timestamp": "2026-01-19T21:30:00"
                }
                """)
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
    public @interface GetMidpointDoc {}


    /**
     * 결과 공유 API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "결과 공유 API",
            description = "모임 결과 URL을 반환합니다."
    )
    @ApiResponses({
            // SUCCESS
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 결과 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "SUCCESS",
                                    value = """
                {
                  "success": true,
                  "data": {
                    "meetingUrl": "https://mingling.com/meeting/abc123def456"
                  },
                  "timestamp": "2026-01-21T18:00:00"
                }
                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 모임",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
            {
              "success": false,
              "code": "NOT_FOUND",
              "message": "모임을 찾을 수 없습니다.",
              "timestamp": "2026-01-21T18:30:00"
            }
            """)
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
              "timestamp": "2026-01-21T19:00:00"
            }
            """)
                    )
            )
    })
    public @interface ResultMeetingDoc {}

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
                                    description = "입장 성공 및 토큰 발급",
                                    value = """
                        {
                          "success": true,
                          "data": {
                            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                          },
                          "timestamp": "2026-01-21T20:30:00"
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
                                            description = "필수 값 누락 또는 유효성 검사 실패",
                                            value = """
                                {
                                  "success": false,
                                  "code": "VALIDATION_ERROR",
                                  "message": "이름은 필수입니다.",
                                  "timestamp": "2026-01-21T20:30:00"
                                }
                                """
                                    ),
                                    @ExampleObject(
                                            name = "INVALID_PASSWORD",
                                            description = "비밀번호가 일치하지 않음",
                                            value = """
                                {
                                  "success": false,
                                  "code": "INVALID_PASSWORD",
                                  "message": "비밀번호가 일치하지 않습니다.",
                                  "timestamp": "2026-01-21T20:30:00"
                                }
                                """
                                    )
                            }
                    )
            ),

            // NOT_FOUND
            @ApiResponse(
                    responseCode = "404",
                    description = "모임을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "MEETING_NOT_FOUND",
                                    description = "존재하지 않는 모임 UUID",
                                    value = """
                        {
                          "success": false,
                          "code": "MEETING_NOT_FOUND",
                          "message": "모임을 찾을 수 없습니다.",
                          "timestamp": "2026-01-21T20:30:00"
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
                            examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "INTERNAL_SERVER_ERROR",
                  "message": "서버 내부 오류가 발생했습니다.",
                  "timestamp": "2026-01-21T20:30:00"
                }
                """)
                    )
            )
    })
    public @interface EnterMeetingDoc {}



    /**
     * 장소 추천 API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "장소 추천 API",
            description = "주변 추천 장소를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 추천 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "SUCCESS",
                                    value = """
                {
                  "success": true,
                  "data": [
                      {
                        "title": "카페1",
                        "roadAddress": "서울 동작구 동작대로..."
                      },
                      {
                        "title": "카페2",
                        "roadAddress": "서울 서초구 방배천로..."
                      }
                  ],
                  "timestamp": "2026-01-22T16:00:00"
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 모임",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "NOT_FOUND",
                  "message": "모임을 찾을 수 없습니다.",
                  "timestamp": "2026-01-22T16:30:00"
                }
                """)
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
                  "timestamp": "2026-01-22T17:00:00"
                }
                """)
                    )
            )
    })
    public @interface GetRecommendDoc {}

    /**
     * 모임 참여 현황 조회 API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "모임 참여 현황 조회 API",
        description = "모임의 현재 참여 현황을 조회합니다. " +
            "현재 참여자 수, 모임 마감 시간, 참여자 이름 및 출발역(위도/경도)을 반환합니다. "
    )
    @ApiResponses({
        // SUCCESS
        @ApiResponse(
            responseCode = "200",
            description = "모임 참여 현황 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                examples = @ExampleObject(
                    name = "SUCCESS",
                    description = "모임 참여 현황 조회 성공",
                    value = """
                    {
                      "success": true,
                      "data": {
                        "totalParticipantCount": 10,
                        "currentParticipantCount": 2,
                        "deadlineAt": "2026-01-23T18:00:00",
                        "participants": [
                          {
                            "userName": "김밍글",
                            "stationName": "구로디지털단지역",
                            "latitude": 37.485266,
                            "longitude": 126.901401
                          },
                          {
                            "userName": "이밍글",
                            "stationName": "합정역",
                            "latitude": 37.549556,
                            "longitude": 126.913878
                          }
                        ]
                      },
                      "timestamp": "2026-01-23T23:00:00"
                    }
                    """
                )
            )
        ),

        // NOT_FOUND
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 모임",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "MEETING_NOT_FOUND",
                  "message": "모임을 찾을 수 없습니다.",
                  "timestamp": "2026-01-23T23:00:00"
                }
                """)
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
                  "timestamp": "2026-01-23T23:00:00"
                }
                """)
            )
        )
    })
    public @interface GetMeetingStatusDoc {}

}

