package swyp.mingling.domain.meeting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swyp.mingling.domain.meeting.dto.request.CreateMeetingRequest;
import swyp.mingling.domain.meeting.dto.request.EnterMeetingRequest;
import swyp.mingling.domain.meeting.dto.response.CreateMeetingResponse;
import swyp.mingling.domain.meeting.dto.response.EnterMeetingResponse;
import swyp.mingling.domain.meeting.dto.response.GetMeetingStatusResponse;
import swyp.mingling.domain.meeting.dto.response.GetMidpointResponse;
import swyp.mingling.domain.meeting.dto.response.RecommendResponse;
import swyp.mingling.domain.meeting.dto.response.ResultMeetingResponse;
import swyp.mingling.global.documentation.MeetingApiDocumentation;
import swyp.mingling.global.exception.BusinessException;
import swyp.mingling.global.response.ApiResponse;

/**
 * 모임 관련 API 컨트롤러
 */
@Tag(name = "모임 API", description = "모임 생성 및 관리 API")
@RestController
@RequestMapping("/meeting")
public class MeetingController {

    /**
     * 모임 생성 API
     *
     * @param request 모임 생성 요청 DTO
     * @return 생성된 모임 URL 응답
     */
    @MeetingApiDocumentation.CreateMeetingDoc
    @PostMapping
    public ApiResponse<CreateMeetingResponse> createMeeting(@Valid @RequestBody CreateMeetingRequest request) {
        return ApiResponse.success(new CreateMeetingResponse());
    }

    /**
     * 중간지점 조회 API
     *
     * @param meetingId 모임 식별자 (UUID)
     * @return 중간지점 번화가 및 추천 장소 목록
     */
    @MeetingApiDocumentation.GetMidpointDoc
    @GetMapping("/{meetingId}/midpoint")
    public ApiResponse<GetMidpointResponse> getMidpoint(@PathVariable("meetingId") UUID meetingId,
                                                        @RequestParam(value = "userName", required = false) String name) {
        // TODO: 실제 로직 구현 필요
        // 1. meetingId로 모임 존재 여부 확인
        // 2. name + meetingId로 기존 참여자 조회(확인필요)
        // 3-1. 참여자가 조회가 된다면: 중간 지점 이름과 위도 경도만 전달
        // 3-2. 참여자가 null이면 : 중간 지점까지의 경로 계산 후 전달

        List<GetMidpointResponse.MidpointDto> mockMidpoints = List.of(
                new GetMidpointResponse.MidpointDto("합정역", 37.5484757, 126.912071, 30, "2호선 > 6호선"),
                new GetMidpointResponse.MidpointDto("서울역", 37.554648, 126.972559, 35, "1호선 > 4호선"),
                new GetMidpointResponse.MidpointDto("용산역", 37.529844, 126.964804, 32, "경의중앙선 > 1호선")
        );



        GetMidpointResponse response = new GetMidpointResponse(mockMidpoints);

        return ApiResponse.success(response);
    }

    /**
     * 모임 결과 공유 API
     *
     * @return 생성된 모임 URL 응답
     */
    @MeetingApiDocumentation.ResultMeetingDoc
    @GetMapping("/{meetingId}/result")
    public ApiResponse<ResultMeetingResponse> resultMeeting(@PathVariable("meetingId") UUID meetingId) {
        // 목 데이터 응답
        String mockUrl = "https://mingling.com/meeting/abc123def456";
        return ApiResponse.success(new ResultMeetingResponse(mockUrl));
    }

    /**
     * 모임 입장하기 API
     *
     * @param meetingId 모임 UUID
     * @param request   입장 요청 DTO (이름, 비밀번호)
     * @return JWT 토큰
     */
    @MeetingApiDocumentation.EnterMeetingDoc
    @PostMapping("/{meetingId}/enter")
    public ApiResponse<EnterMeetingResponse> enterMeeting(
            @PathVariable("meetingId") UUID meetingId,
            @Valid @RequestBody EnterMeetingRequest request) {

        // TODO: 실제 로직 구현 필요
        // 1. meetingId로 모임 존재 여부 확인
        // 2. name + meetingId로 기존 참여자 조회(확인필요)
        // 3-1. 기존 참여자가 있으면: 비밀번호 검증 후 토큰 발급
        // 3-2. 기존 참여자가 없으면: 신규 참여자 생성 후 토큰 발급

        // 목 데이터 응답
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkhvbmcgR2lsZG9uZyIsImlhdCI6MTUxNjIzOTAyMn0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        return ApiResponse.success(new EnterMeetingResponse(mockToken));
    }


    /**
     * 장소 추천 API
     *
     * @param meetingId 모임 UUID
     * @param midPlace  중간 지점 장소 (query param)
     * @param category  모임 목적 (query param)
     * @return 장소 추천 장소들 목록
     */
    @MeetingApiDocumentation.GetRecommendDoc
    @GetMapping("/{meetingId}/recommend")
    public ApiResponse<List<RecommendResponse>> getRecommend(@PathVariable("meetingId") UUID meetingId,
                                                             @RequestParam String midPlace,
                                                             @RequestParam String category) {

        List<RecommendResponse> recommendResponses = List.of(
                new RecommendResponse("카페1", "서울 동작구 동작대로..."),
                new RecommendResponse("카페2", "서울 서초구 방배천로...")
        );

        return ApiResponse.success(recommendResponses);
    }

    /**
     * 모임 참여 현황 조회 API
     *
     * @param meetingId 모임 UUID
     * @return 참여현황 정보
     */
    @MeetingApiDocumentation.GetMeetingStatusDoc
    @GetMapping("/{meetingId}/status")
    public ApiResponse<GetMeetingStatusResponse> getMeetingStatus(@PathVariable("meetingId") UUID meetingId) {

        // TODO: 로그인 여부 확인 (토큰 있으면 참여자 식별)

        // MEETING_NOT_FOUND
        if (meetingId.toString().equals("00000000-0000-0000-0000-000000000404")) {
            throw BusinessException.meetingNotFound();
        }

        GetMeetingStatusResponse response = GetMeetingStatusResponse.of(
            10,
            2,
            LocalDateTime.now(),
            List.of(
                GetMeetingStatusResponse.ParticipantInfo.of(
                    "김밍글",
                    "구로디지털단지역",
                    37.485266,
                    126.901401
                ),
                GetMeetingStatusResponse.ParticipantInfo.of(
                    "이밍글",
                    "합정역",
                    37.549556,
                    126.913878
                )
            )
        );

        return ApiResponse.success(response);
    }
}

