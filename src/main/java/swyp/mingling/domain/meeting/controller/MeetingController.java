package swyp.mingling.domain.meeting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import swyp.mingling.domain.meeting.dto.request.CreateMeetingRequest;
import swyp.mingling.domain.meeting.dto.request.EnterMeetingRequest;
import swyp.mingling.domain.meeting.dto.response.CreateMeetingResponse;
import swyp.mingling.domain.meeting.dto.response.EnterMeetingResponse;
import swyp.mingling.domain.meeting.dto.response.GetMidpointResponse;
import swyp.mingling.domain.meeting.dto.response.ResultMeetingResponse;
import swyp.mingling.global.documentation.MeetingApiDocumentation;
import swyp.mingling.global.response.ApiResponse;

import java.util.List;
import java.util.UUID;

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
    public ApiResponse<GetMidpointResponse> getMidpoint(@PathVariable("meetingId") UUID meetingId) {

        List<GetMidpointResponse.MidpointDto> mockMidpoints = List.of(
                new GetMidpointResponse.MidpointDto("합정역", 37.5484757, 126.912071, 30, "2호선 > 6호선"),
                new GetMidpointResponse.MidpointDto("서울역", 37.554648, 126.972559, 35, "1호선 > 4호선"),
                new GetMidpointResponse.MidpointDto("용산역", 37.529844, 126.964804, 32, "경의중앙선 > 1호선")
        );

        List<GetMidpointResponse.RecommendationDto> mockRecommendations = List.of(
                new GetMidpointResponse.RecommendationDto("합정 맛집 카페", "음식점 > 카페", "서울특별시 마포구 양화로..."),
                new GetMidpointResponse.RecommendationDto("서울역 비즈니스 호텔", "숙박 > 호텔", "서울특별시 중구 한강대로...")
        );

        GetMidpointResponse response = new GetMidpointResponse(mockMidpoints, mockRecommendations);

        return ApiResponse.success(response);
    }

    /**
     * 모임 결과 공유 API
     *
     * @return 생성된 모임 URL 응답
     */
    @MeetingApiDocumentation.ResultMeetingDoc
    @GetMapping("/{meetingId}/result")
    public ApiResponse<ResultMeetingResponse> resultMeeting() {
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
}

