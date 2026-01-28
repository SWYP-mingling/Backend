package swyp.mingling.domain.meeting.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swyp.mingling.domain.meeting.dto.request.CreateMeetingRequest;
import swyp.mingling.domain.meeting.dto.response.*;
import swyp.mingling.domain.meeting.service.CreateMeetingUseCase;
import swyp.mingling.domain.meeting.service.ResultMeetingUseCase;
import swyp.mingling.global.documentation.MeetingApiDocumentation;
import swyp.mingling.global.exception.BusinessException;
import swyp.mingling.global.response.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 모임 관련 API 컨트롤러
 */
@Tag(name = "모임 API", description = "모임 생성 및 관리 API")
@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final CreateMeetingUseCase createMeetingUseCase;
    private final ResultMeetingUseCase resultMeetingUseCase;

    /**
     * 모임 생성 API
     *
     * @param request 모임 생성 요청 DTO
     * @return 생성된 모임 URL 응답
     */
    @MeetingApiDocumentation.CreateMeetingDoc
    @PostMapping
    public ApiResponse<CreateMeetingResponse> createMeeting(
            @Valid @RequestBody CreateMeetingRequest request) {
        CreateMeetingResponse response = createMeetingUseCase.createMeeting(request);
        return ApiResponse.success(response);
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
                                                        @Parameter(hidden = true)
                                                        @SessionAttribute(value = "userName") String userName) {
        // TODO: 실제 로직 구현 필요
        // 1. meetingId로 모임 존재 여부 확인
        // 2. name + meetingId로 기존 참여자 조회(확인필요)
        // 3. 중간 지점 이름과 위도 경도, 로그인한 참여자의 출발 위치부터 중간지점 까지의 경로 전달

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
     * @param meetingId 모임 UUID
     * @return 생성된 모임 URL 응답
     */
    @MeetingApiDocumentation.ResultMeetingDoc
    @GetMapping("/{meetingId}/result")
    public ApiResponse<ResultMeetingResponse> resultMeeting(@PathVariable("meetingId") String meetingId) {
        ResultMeetingResponse response = resultMeetingUseCase.getResultMeetingUrl(meetingId);
        return ApiResponse.success(response);
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

