package swyp.mingling.global.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
            description = "새로운 모임을 생성하고 모임 URL을 반환합니다. 사용자당 여러 개의 모임 목적을 지정할 수 있습니다.",
            requestBody = @RequestBody(
                    description = "모임 생성 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = swyp.mingling.domain.meeting.dto.request.CreateMeetingRequest.class),
                            examples = @ExampleObject(
                                    name = "모임 생성 요청 예시",
                                    description = "여러 개의 모임 목적을 지정한 모임 생성 요청",
                                    value = """
                                    {
                                      "meetingName": "신년회",
                                      "purposes": ["친목", "회식", "스터디"],
                                      "purposeCount": 3,
                                      "capacity": 10,
                                      "deadline": "2026-01-30T23:59:59"
                                    }
                                    """
                            )
                    )
            )
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
                        "meetingUrl": "https://mingling.com/meeting/62db1c35-f7db-4aad-acc8-0ad64f61a312",
                        "meetingId": "62db1c35-f7db-4aad-acc8-0ad64f61a312"
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
                                            name = "VALIDATION_ERROR_DEADLINE",
                                            description = "마감 시간 검증 실패",
                                            value = """
                        {
                          "success": false,
                          "code": "BAD_REQUEST",
                          "message": "마감 시간은 현재 시간 이후여야 합니다.",
                          "timestamp": "2026-01-29T02:26:25"
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "VALIDATION_ERROR_CAPACITY",
                                            description = "정원 검증 실패",
                                            value = """
                        {
                          "success": false,
                          "code": "BAD_REQUEST",
                          "message": "모임 인원은 최소 2명 이상이어야 합니다.",
                          "timestamp": "2026-01-29T02:26:25"
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "VALIDATION_ERROR_NAME",
                                            description = "모임명 누락",
                                            value = """
                        {
                          "success": false,
                          "code": "BAD_REQUEST",
                          "message": "모임명은 필수입니다.",
                          "timestamp": "2026-01-29T02:26:25"
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "VALIDATION_ERROR_PURPOSES",
                                            description = "모임 목적 리스트 누락",
                                            value = """
                        {
                          "success": false,
                          "code": "BAD_REQUEST",
                          "message": "모임 목적 리스트는 필수입니다.",
                          "timestamp": "2026-01-29T02:26:25"
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "VALIDATION_ERROR_PURPOSE_COUNT",
                                            description = "모임 목적 개수 누락",
                                            value = """
                        {
                          "success": false,
                          "code": "BAD_REQUEST",
                          "message": "모임 목적 개수는 필수입니다.",
                          "timestamp": "2026-01-29T02:26:25"
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "PURPOSE_NOT_FOUND",
                                            description = "존재하지 않는 모임 목적",
                                            value = """
                        {
                          "success": false,
                          "code": "PURPOSE_NOT_FOUND",
                          "message": "일부 모임 목적을 찾을 수 없습니다.",
                          "timestamp": "2026-01-29T02:26:25"
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
                  "timestamp": "2026-01-29T02:26:25"
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
                    responseCode = "401",
                    description = "사용자 인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "USER_UNAUTHORIZED",
                  "message": "사용자 인증에 실패했습니다.",
                  "timestamp": "2026-01-29T02:26:25"
                }
                """)
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
                  "timestamp": "2026-01-29T02:26:25"
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
                  "timestamp": "2026-01-29T02:26:25"
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
                    "meetingUrl": "https://mingling.com/meeting/62db1c35-f7db-4aad-acc8-0ad64f61a312"
                  },
                  "timestamp": "2026-01-21T18:00:00"
                }
                """
                            )
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
              "timestamp": "2026-01-29T02:26:25"
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
              "timestamp": "2026-01-29T02:26:25"
            }
            """)
                    )
            )
    })
    public @interface ResultMeetingDoc {}

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
                    responseCode = "401",
                    description = "사용자 인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "USER_UNAUTHORIZED",
                  "message": "사용자 인증에 실패했습니다.",
                  "data": null,
                  "timestamp": "2026-01-22T16:30:00"
                }
                """)
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
                  "data": null,
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
                  "data": null,
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
                        "pendingParticipantCount": 8,
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

        // UNAUTHORIZED
        @ApiResponse(
            responseCode = "401",
            description = "사용자 인증 실패",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject("""
                {
                  "success": false,
                  "code": "USER_UNAUTHORIZED",
                  "message": "사용자 인증에 실패했습니다.",
                  "data": null,
                  "timestamp": "2026-01-23T23:00:00"
                }
                """)
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
                  "data": null,
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
                  "data": null,
                  "timestamp": "2026-01-23T23:00:00"
                }
                """)
            )
        )
    })
    public @interface GetMeetingStatusDoc {}

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

}

