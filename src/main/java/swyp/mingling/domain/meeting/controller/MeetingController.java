package swyp.mingling.domain.meeting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import swyp.mingling.domain.meeting.dto.request.CreateDepartureRequest;
import swyp.mingling.domain.meeting.dto.request.CreateMeetingRequest;
import swyp.mingling.domain.meeting.dto.request.UpdateDepartureRequest;
import swyp.mingling.domain.meeting.dto.response.*;
import swyp.mingling.domain.meeting.dto.response.midpoint.GetMidPointResponse;
import swyp.mingling.domain.meeting.service.*;
import swyp.mingling.global.documentation.MeetingApiDocumentation;
import swyp.mingling.global.response.ApiResponse;

import java.util.List;
import java.util.UUID;

/**
 * 모임 관련 API 컨트롤러
 */
@Slf4j
@Tag(name = "모임 API", description = "모임 생성 및 관리 API")
@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final CreateMeetingUseCase createMeetingUseCase;
    private final ResultMeetingUseCase resultMeetingUseCase;
    private final CreateDepartureUseCase createDepartureUseCase;
    private final UpdateDepartureUseCase updateDepartureUseCase;
    private final DeleteDepartureUseCase deleteDepartureUseCase;
    private final GetMeetingStatusUseCase getMeetingStatusUseCase;
    private final RecommendPlaceUseCase recommendPlaceUseCase;
    private final MidPointAsyncUseCase midPointAsyncUseCase;
    private final GuestStatusUseCase guestStatusUseCase;

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
     * 중간지점 조회 API (동기 버전)
     *
     * @param meetingId 모임 식별자 (UUID)
     * @return 중간지점 번화가 및 추천 장소 목록
     */
    @MeetingApiDocumentation.GetMidpointDoc
    @GetMapping("/{meetingId}/midpoint")
    public ApiResponse<Object> getMidpoint(@PathVariable("meetingId") UUID meetingId) {

        List<GetMidPointResponse> execute = midPointAsyncUseCase.execute(meetingId);


        return ApiResponse.success(execute);
    }

    /**
     * 모임 결과 공유 API
     *
     * @param meetingId 모임 UUID
     * @return 생성된 모임 URL 응답
     */
    @MeetingApiDocumentation.ResultMeetingDoc
    @GetMapping("/result/{meetingId}")
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
     * @param page      조회할 페이지 번호 (1부터 시작)
     * @param size      조회할 개수 (기본값 10)
     * @return 장소 추천 장소들 목록
     */
    @MeetingApiDocumentation.GetRecommendDoc
    @GetMapping("/{meetingId}/recommend")
    public ApiResponse<RecommendResponse> getRecommend(@PathVariable("meetingId") UUID meetingId,
                                                       @RequestParam String midPlace,
                                                       @RequestParam String category,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "15") int size) {
        RecommendResponse response = recommendPlaceUseCase.execute(midPlace, category, page, size);
        return ApiResponse.success(response);
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
        GetMeetingStatusResponse response = getMeetingStatusUseCase.execute(meetingId);
        return ApiResponse.success(response);
    }

    /**
     * 출발역 등록 API
     *
     * @param meetingId 모임 UUID
     * @param request 출발역 등록 요청 DTO
     * @return 출발역 등록 응답 DTO
     */
    @MeetingApiDocumentation.CreateDepartureDoc
    @PostMapping("/{meetingId}/departure")
    public ApiResponse<CreateDepartureResponse> createDeparture(
            @PathVariable("meetingId") UUID meetingId,
            HttpSession session,
            @Valid @RequestBody CreateDepartureRequest request) {

        //세션에서 nickname 가져오기
        String nickname = (String) session.getAttribute(String.valueOf(meetingId));

        CreateDepartureResponse response = createDepartureUseCase.execute(meetingId, nickname, request);

        return ApiResponse.success(response);
    }

    /**
     * 출발역 수정 API
     *
     * @param request 출발역 수정 요청 DTO
     * @return 사용자정보, 출발역 정보
     */
    @MeetingApiDocumentation.UpdateDepartDoc
    @PatchMapping("/{meetingId}/departure")
    public ApiResponse<UpdateDepartureResponse> updateDeparture(
            @PathVariable("meetingId") UUID meetingId,
            HttpSession session,
            @Valid @RequestBody UpdateDepartureRequest request) {

        //세션에서 nickname 가져오기
        String nickname = (String) session.getAttribute(String.valueOf(meetingId));

        UpdateDepartureResponse response = updateDepartureUseCase.execute(meetingId, nickname, request);

        return ApiResponse.success(response);

    }

    /**
     * 출발역 삭제 API
     *
     * @return 사용자 닉네임
     */
    @MeetingApiDocumentation.DeleteDepartDoc
    @DeleteMapping("/{meetingId}/departure")
    public ApiResponse<String> updateDeparture(
            @PathVariable("meetingId") UUID meetingId,
            HttpSession session) {

        //세션에서 nickname 가져오기
        String nickname = (String) session.getAttribute(String.valueOf(meetingId));

        String deletedNickname = deleteDepartureUseCase.execute(meetingId, nickname);

        return ApiResponse.success(deletedNickname);

    }

    @GetMapping("/{meetingId}/guestStatus")
    public ApiResponse<GuestStatusResponse> gueststatus(
            @PathVariable("meetingId") UUID meetingId) {

        GuestStatusResponse execute = guestStatusUseCase.execute(meetingId);

        return ApiResponse.success(execute);

    }
}

