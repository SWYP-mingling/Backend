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
                                      "purposes": ["맛집", "술집"],
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
                        "meetingUrl": "https://mingling.kr/meeting/62db1c35-f7db-4aad-acc8-0ad64f61a312",
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
                                                             "data": [
                                                                 {
                                                                     "endStation": "압구정",
                                                                     "endStationLine": "3호선",
                                                                     "latitude": 37.527072,
                                                                     "longitude": 127.028461,
                                                                     "userRoutes": [
                                                                         {
                                                                             "nickname": "안녕",
                                                                             "startStation": "건대입구",
                                                                             "startStationLine": "7호선",
                                                                             "latitude": 37.540373,
                                                                             "longitude": 127.069191,
                                                                             "travelTime": 23,
                                                                             "transferPath": [
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "고속터미널",
                                                                                     "latitude": 37.504891,
                                                                                     "longitude": 127.004916
                                                                                 }
                                                                             ],
                                                                             "stations": [
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "건대입구",
                                                                                     "latitude": 37.540373,
                                                                                     "longitude": 127.069191
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "자양",
                                                                                     "latitude": 37.53154,
                                                                                     "longitude": 127.066704
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "청담",
                                                                                     "latitude": 37.519365,
                                                                                     "longitude": 127.05335
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "강남구청",
                                                                                     "latitude": 37.517179,
                                                                                     "longitude": 127.041255
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "학동",
                                                                                     "latitude": 37.514229,
                                                                                     "longitude": 127.031656
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "논현",
                                                                                     "latitude": 37.511093,
                                                                                     "longitude": 127.021415
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "반포",
                                                                                     "latitude": 37.508178,
                                                                                     "longitude": 127.011727
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "고속터미널",
                                                                                     "latitude": 37.504891,
                                                                                     "longitude": 127.004916
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "고속터미널",
                                                                                     "latitude": 37.504891,
                                                                                     "longitude": 127.004916
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "잠원",
                                                                                     "latitude": 37.512759,
                                                                                     "longitude": 127.01122
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "신사",
                                                                                     "latitude": 37.516334,
                                                                                     "longitude": 127.020114
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "압구정",
                                                                                     "latitude": 37.527072,
                                                                                     "longitude": 127.028461
                                                                                 }
                                                                             ]
                                                                         },
                                                                         {
                                                                             "nickname": "안녕1",
                                                                             "startStation": "강남",
                                                                             "startStationLine": "2호선",
                                                                             "latitude": 37.49799,
                                                                             "longitude": 127.027912,
                                                                             "travelTime": 11,
                                                                             "transferPath": [
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "교대",
                                                                                     "latitude": 37.493961,
                                                                                     "longitude": 127.014667
                                                                                 }
                                                                             ],
                                                                             "stations": [
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "강남",
                                                                                     "latitude": 37.49799,
                                                                                     "longitude": 127.027912
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "교대",
                                                                                     "latitude": 37.493961,
                                                                                     "longitude": 127.014667
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "교대",
                                                                                     "latitude": 37.493961,
                                                                                     "longitude": 127.014667
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "고속터미널",
                                                                                     "latitude": 37.504891,
                                                                                     "longitude": 127.004916
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "잠원",
                                                                                     "latitude": 37.512759,
                                                                                     "longitude": 127.01122
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "신사",
                                                                                     "latitude": 37.516334,
                                                                                     "longitude": 127.020114
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "압구정",
                                                                                     "latitude": 37.527072,
                                                                                     "longitude": 127.028461
                                                                                 }
                                                                             ]
                                                                         },
                                                                         {
                                                                             "nickname": "안녕2",
                                                                             "startStation": "홍대입구",
                                                                             "startStationLine": "2호선",
                                                                             "latitude": 37.55679,
                                                                             "longitude": 126.923708,
                                                                             "travelTime": 26,
                                                                             "transferPath": [
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "을지로3가",
                                                                                     "latitude": 37.566306,
                                                                                     "longitude": 126.991696
                                                                                 }
                                                                             ],
                                                                             "stations": [
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "홍대입구",
                                                                                     "latitude": 37.55679,
                                                                                     "longitude": 126.923708
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "신촌",
                                                                                     "latitude": 37.555131,
                                                                                     "longitude": 126.936926
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "이대",
                                                                                     "latitude": 37.556733,
                                                                                     "longitude": 126.946013
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "아현",
                                                                                     "latitude": 37.557345,
                                                                                     "longitude": 126.956141
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "충정로",
                                                                                     "latitude": 37.559704,
                                                                                     "longitude": 126.964378
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "시청",
                                                                                     "latitude": 37.565715,
                                                                                     "longitude": 126.977088
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "을지로입구",
                                                                                     "latitude": 37.566014,
                                                                                     "longitude": 126.982618
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "을지로3가",
                                                                                     "latitude": 37.566306,
                                                                                     "longitude": 126.991696
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "을지로3가",
                                                                                     "latitude": 37.566306,
                                                                                     "longitude": 126.991696
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "충무로",
                                                                                     "latitude": 37.56143,
                                                                                     "longitude": 126.994072
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "동대입구",
                                                                                     "latitude": 37.559052,
                                                                                     "longitude": 127.005602
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "약수",
                                                                                     "latitude": 37.554867,
                                                                                     "longitude": 127.010541
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "금호",
                                                                                     "latitude": 37.548034,
                                                                                     "longitude": 127.015872
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "옥수",
                                                                                     "latitude": 37.541684,
                                                                                     "longitude": 127.017269
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "압구정",
                                                                                     "latitude": 37.527072,
                                                                                     "longitude": 127.028461
                                                                                 }
                                                                             ]
                                                                         }
                                                                     ]
                                                                 },
                                                                 {
                                                                     "endStation": "이태원",
                                                                     "endStationLine": "6호선",
                                                                     "latitude": 37.534488,
                                                                     "longitude": 126.994302,
                                                                     "userRoutes": [
                                                                         {
                                                                             "nickname": "안녕",
                                                                             "startStation": "건대입구",
                                                                             "startStationLine": "2호선",
                                                                             "latitude": 37.540373,
                                                                             "longitude": 127.069191,
                                                                             "travelTime": 26,
                                                                             "transferPath": [
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "신당",
                                                                                     "latitude": 37.56564,
                                                                                     "longitude": 127.019614
                                                                                 }
                                                                             ],
                                                                             "stations": [
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "건대입구",
                                                                                     "latitude": 37.540373,
                                                                                     "longitude": 127.069191
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "성수",
                                                                                     "latitude": 37.544581,
                                                                                     "longitude": 127.055961
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "뚝섬",
                                                                                     "latitude": 37.547184,
                                                                                     "longitude": 127.047367
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "한양대",
                                                                                     "latitude": 37.555273,
                                                                                     "longitude": 127.043655
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "왕십리",
                                                                                     "latitude": 37.561238,
                                                                                     "longitude": 127.036954
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "상왕십리",
                                                                                     "latitude": 37.564354,
                                                                                     "longitude": 127.029354
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "신당",
                                                                                     "latitude": 37.56564,
                                                                                     "longitude": 127.019614
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "신당",
                                                                                     "latitude": 37.56564,
                                                                                     "longitude": 127.019614
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "청구",
                                                                                     "latitude": 37.560276,
                                                                                     "longitude": 127.013639
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "약수",
                                                                                     "latitude": 37.554867,
                                                                                     "longitude": 127.010541
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "버티고개",
                                                                                     "latitude": 37.548013,
                                                                                     "longitude": 127.007055
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "한강진",
                                                                                     "latitude": 37.539631,
                                                                                     "longitude": 127.001725
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "이태원",
                                                                                     "latitude": 37.534488,
                                                                                     "longitude": 126.994302
                                                                                 }
                                                                             ]
                                                                         },
                                                                         {
                                                                             "nickname": "안녕1",
                                                                             "startStation": "강남",
                                                                             "startStationLine": "2호선",
                                                                             "latitude": 37.49799,
                                                                             "longitude": 127.027912,
                                                                             "travelTime": 33,
                                                                             "transferPath": [
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "교대",
                                                                                     "latitude": 37.493961,
                                                                                     "longitude": 127.014667
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "약수",
                                                                                     "latitude": 37.554867,
                                                                                     "longitude": 127.010541
                                                                                 }
                                                                             ],
                                                                             "stations": [
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "강남",
                                                                                     "latitude": 37.49799,
                                                                                     "longitude": 127.027912
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "교대",
                                                                                     "latitude": 37.493961,
                                                                                     "longitude": 127.014667
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "교대",
                                                                                     "latitude": 37.493961,
                                                                                     "longitude": 127.014667
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "고속터미널",
                                                                                     "latitude": 37.504891,
                                                                                     "longitude": 127.004916
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "잠원",
                                                                                     "latitude": 37.512759,
                                                                                     "longitude": 127.01122
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "신사",
                                                                                     "latitude": 37.516334,
                                                                                     "longitude": 127.020114
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "압구정",
                                                                                     "latitude": 37.527072,
                                                                                     "longitude": 127.028461
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "옥수",
                                                                                     "latitude": 37.541684,
                                                                                     "longitude": 127.017269
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "금호",
                                                                                     "latitude": 37.548034,
                                                                                     "longitude": 127.015872
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "약수",
                                                                                     "latitude": 37.554867,
                                                                                     "longitude": 127.010541
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "약수",
                                                                                     "latitude": 37.554867,
                                                                                     "longitude": 127.010541
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "버티고개",
                                                                                     "latitude": 37.548013,
                                                                                     "longitude": 127.007055
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "한강진",
                                                                                     "latitude": 37.539631,
                                                                                     "longitude": 127.001725
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "이태원",
                                                                                     "latitude": 37.534488,
                                                                                     "longitude": 126.994302
                                                                                 }
                                                                             ]
                                                                         },
                                                                         {
                                                                             "nickname": "안녕2",
                                                                             "startStation": "홍대입구",
                                                                             "startStationLine": "공항철도",
                                                                             "latitude": 37.55679,
                                                                             "longitude": 126.923708,
                                                                             "travelTime": 14,
                                                                             "transferPath": [
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "공덕",
                                                                                     "latitude": 37.544431,
                                                                                     "longitude": 126.951372
                                                                                 }
                                                                             ],
                                                                             "stations": [
                                                                                 {
                                                                                     "linenumber": "공항철도",
                                                                                     "station": "홍대입구",
                                                                                     "latitude": 37.55679,
                                                                                     "longitude": 126.923708
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "공항철도",
                                                                                     "station": "공덕",
                                                                                     "latitude": 37.544431,
                                                                                     "longitude": 126.951372
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "공덕",
                                                                                     "latitude": 37.544431,
                                                                                     "longitude": 126.951372
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "효창공원앞",
                                                                                     "latitude": 37.539233,
                                                                                     "longitude": 126.961384
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "삼각지",
                                                                                     "latitude": 37.534075,
                                                                                     "longitude": 126.9726
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "녹사평",
                                                                                     "latitude": 37.534675,
                                                                                     "longitude": 126.986695
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "6호선",
                                                                                     "station": "이태원",
                                                                                     "latitude": 37.534488,
                                                                                     "longitude": 126.994302
                                                                                 }
                                                                             ]
                                                                         }
                                                                     ]
                                                                 },
                                                                 {
                                                                     "endStation": "신사",
                                                                     "endStationLine": "신분당선",
                                                                     "latitude": 37.516334,
                                                                     "longitude": 127.020114,
                                                                     "userRoutes": [
                                                                         {
                                                                             "nickname": "안녕",
                                                                             "startStation": "건대입구",
                                                                             "startStationLine": "7호선",
                                                                             "latitude": 37.540373,
                                                                             "longitude": 127.069191,
                                                                             "travelTime": 16,
                                                                             "transferPath": [
                                                                                 {
                                                                                     "linenumber": "신분당선",
                                                                                     "station": "논현",
                                                                                     "latitude": 37.511093,
                                                                                     "longitude": 127.021415
                                                                                 }
                                                                             ],
                                                                             "stations": [
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "건대입구",
                                                                                     "latitude": 37.540373,
                                                                                     "longitude": 127.069191
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "자양",
                                                                                     "latitude": 37.53154,
                                                                                     "longitude": 127.066704
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "청담",
                                                                                     "latitude": 37.519365,
                                                                                     "longitude": 127.05335
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "강남구청",
                                                                                     "latitude": 37.517179,
                                                                                     "longitude": 127.041255
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "학동",
                                                                                     "latitude": 37.514229,
                                                                                     "longitude": 127.031656
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "7호선",
                                                                                     "station": "논현",
                                                                                     "latitude": 37.511093,
                                                                                     "longitude": 127.021415
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "신분당선",
                                                                                     "station": "논현",
                                                                                     "latitude": 37.511093,
                                                                                     "longitude": 127.021415
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "신분당선",
                                                                                     "station": "신사",
                                                                                     "latitude": 37.516334,
                                                                                     "longitude": 127.020114
                                                                                 }
                                                                             ]
                                                                         },
                                                                         {
                                                                             "nickname": "안녕1",
                                                                             "startStation": "강남",
                                                                             "startStationLine": "신분당선",
                                                                             "latitude": 37.49799,
                                                                             "longitude": 127.027912,
                                                                             "travelTime": 4,
                                                                             "transferPath": [],
                                                                             "stations": [
                                                                                 {
                                                                                     "linenumber": "신분당선",
                                                                                     "station": "강남",
                                                                                     "latitude": 37.49799,
                                                                                     "longitude": 127.027912
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "신분당선",
                                                                                     "station": "신논현",
                                                                                     "latitude": 37.504598,
                                                                                     "longitude": 127.02506
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "신분당선",
                                                                                     "station": "논현",
                                                                                     "latitude": 37.511093,
                                                                                     "longitude": 127.021415
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "신분당선",
                                                                                     "station": "신사",
                                                                                     "latitude": 37.516334,
                                                                                     "longitude": 127.020114
                                                                                 }
                                                                             ]
                                                                         },
                                                                         {
                                                                             "nickname": "안녕2",
                                                                             "startStation": "홍대입구",
                                                                             "startStationLine": "2호선",
                                                                             "latitude": 37.55679,
                                                                             "longitude": 126.923708,
                                                                             "travelTime": 29,
                                                                             "transferPath": [
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "을지로3가",
                                                                                     "latitude": 37.566306,
                                                                                     "longitude": 126.991696
                                                                                 }
                                                                             ],
                                                                             "stations": [
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "홍대입구",
                                                                                     "latitude": 37.55679,
                                                                                     "longitude": 126.923708
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "신촌",
                                                                                     "latitude": 37.555131,
                                                                                     "longitude": 126.936926
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "이대",
                                                                                     "latitude": 37.556733,
                                                                                     "longitude": 126.946013
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "아현",
                                                                                     "latitude": 37.557345,
                                                                                     "longitude": 126.956141
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "충정로",
                                                                                     "latitude": 37.559704,
                                                                                     "longitude": 126.964378
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "시청",
                                                                                     "latitude": 37.565715,
                                                                                     "longitude": 126.977088
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "을지로입구",
                                                                                     "latitude": 37.566014,
                                                                                     "longitude": 126.982618
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "2호선",
                                                                                     "station": "을지로3가",
                                                                                     "latitude": 37.566306,
                                                                                     "longitude": 126.991696
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "을지로3가",
                                                                                     "latitude": 37.566306,
                                                                                     "longitude": 126.991696
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "충무로",
                                                                                     "latitude": 37.56143,
                                                                                     "longitude": 126.994072
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "동대입구",
                                                                                     "latitude": 37.559052,
                                                                                     "longitude": 127.005602
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "약수",
                                                                                     "latitude": 37.554867,
                                                                                     "longitude": 127.010541
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "금호",
                                                                                     "latitude": 37.548034,
                                                                                     "longitude": 127.015872
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "옥수",
                                                                                     "latitude": 37.541684,
                                                                                     "longitude": 127.017269
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "압구정",
                                                                                     "latitude": 37.527072,
                                                                                     "longitude": 127.028461
                                                                                 },
                                                                                 {
                                                                                     "linenumber": "3호선",
                                                                                     "station": "신사",
                                                                                     "latitude": 37.516334,
                                                                                     "longitude": 127.020114
                                                                                 }
                                                                             ]
                                                                         }
                                                                     ]
                                                                 }
                                                             ],
                                                             "timestamp": "2026-02-03T20:48:11"
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
                    "meetingUrl": "https://mingling.kr/meeting/62db1c35-f7db-4aad-acc8-0ad64f61a312"
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
            description = "중간 지점과 카테고리를 기준으로 주변 추천 장소 목록을 조회합니다."
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
                        "data": {
                            "placeInfos": [
                                {
                                    "placeName": "두쫀쿠하우스",
                                    "categoryName": "음식점 > 카페",
                                    "categoryGroupName": "카페",
                                    "phone": "02-1234-4568",
                                    "addressName": "서울 마포구 합정동 123-45",
                                    "roadAddressName": "서울 마포구 월드컵로1길 12",
                                    "placeUrl": "http://place.map.kakao.com/0000000000",
                                    "x": "126.000000000000",
                                    "y": "37.0000000000000"
                                },
                                {
                                    "placeName": "두쫀쿠마켓",
                                    "categoryName": "음식점 > 카페",
                                    "categoryGroupName": "카페",
                                    "phone": "02-9123-4567",
                                    "addressName": "서울 마포구 서교동 456-789",
                                    "roadAddressName": "서울 마포구 양화로12길 3",
                                    "placeUrl": "http://place.map.kakao.com/00000001",
                                    "x": "126.0000000001",
                                    "y": "37.0000000000001"
                                },
                                {
                                    "placeName": "두쫀쿠커피",
                                    "categoryName": "음식점 > 카페 > 커피전문점",
                                    "categoryGroupName": "카페",
                                    "phone": "02-891-2345",
                                    "addressName": "서울 마포구 합정동 123-4",
                                    "roadAddressName": "서울 마포구 동교로 1",
                                    "placeUrl": "http://place.map.kakao.com/0000000002",
                                    "x": "126.0000000000002",
                                    "y": "37.0000000000002"
                                }
                            ],
                            "sliceInfo": {
                                "hasNext": false,
                                "page": 5,
                                "size": 3
                            }
                        },
                        "timestamp": "2026-01-31T05:00:00"
                    }
                """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "지원하지 않는 카카오 카테고리",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "INVALID_KAKAO_CATEGORY",
                    value = """
            {
              "success": false,
              "code": "INVALID_KAKAO_CATEGORY",
              "message": "지원하지 않는 카카오 카테고리입니다.",
              "data": null,
              "timestamp": "2026-01-31T05:00:00"
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
        @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "EXTERNAL_API_ERROR",
                        value = """
                {
                  "success": false,
                  "code": "EXTERNAL_API_ERROR",
                  "message": "외부 API 호출 중 오류가 발생했습니다.",
                  "data": null,
                  "timestamp": "2026-01-30T10:05:00"
                }
                """
                    ),
                    @ExampleObject(
                        name = "INTERNAL_SERVER_ERROR",
                        value = """
                {
                  "success": false,
                  "code": "INTERNAL_SERVER_ERROR",
                  "message": "서버 내부 오류가 발생했습니다.",
                  "data": null,
                  "timestamp": "2026-01-22T17:00:00"
                }
                """
                    )
                }
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

    /**
     * 출발지 삭제 API 문서
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "출발지 삭제 API",
            description = "로그인된 사용자의 세션(nickname)을 기반으로 특정 모임(UUID로 구분)에 참여 중인 사용자의 출발지를 삭제하고 성공시 닉네임을 반환합니다"
    )
    @ApiResponses({
            // SUCCESS
            @ApiResponse(
                    responseCode = "200",
                    description = "출발지 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = swyp.mingling.global.response.ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "SUCCESS",
                                    description = "출발지 수정 성공",
                                    value = """
                    {
                      "success": true,
                      "data": "홍길동",
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

            // DEPARTURE_NOT_FOUND
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자의 출발지가 존재하지 않을 때",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DEPARTURE_NOT_FOUND",
                                    description = "세션 및 쿠키 만료",
                                    value = """
                {
                  "success": false,
                  "code": "DEPARTURE_NOT_FOUND",
                  "message": "사용자의 출발지가 등록되지 않았습니다.",
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
    public @interface DeleteDepartDoc {}

}

