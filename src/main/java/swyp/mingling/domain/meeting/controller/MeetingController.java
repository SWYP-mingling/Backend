package swyp.mingling.domain.meeting.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import swyp.mingling.domain.meeting.dto.request.CreateDepartureRequest;
import swyp.mingling.domain.meeting.dto.request.CreateMeetingRequest;
import swyp.mingling.domain.meeting.dto.request.UpdateDepartureRequest;
import swyp.mingling.domain.meeting.dto.response.CreateDepartureResponse;
import swyp.mingling.domain.meeting.dto.response.CreateMeetingResponse;
import swyp.mingling.domain.meeting.dto.response.GetMeetingStatusResponse;
import swyp.mingling.domain.meeting.dto.response.GetMidpointResponse;
import swyp.mingling.domain.meeting.dto.response.RecommendResponse;
import swyp.mingling.domain.meeting.dto.response.ResultMeetingResponse;
import swyp.mingling.domain.meeting.dto.response.UpdateDepartureResponse;
import swyp.mingling.domain.meeting.service.CreateDepartureUseCase;
import swyp.mingling.domain.meeting.service.CreateMeetingUseCase;
import swyp.mingling.domain.meeting.service.GetMeetingStatusUseCase;
import swyp.mingling.domain.meeting.service.RecommendPlaceUseCase;
import swyp.mingling.domain.meeting.service.ResultMeetingUseCase;
import swyp.mingling.domain.meeting.service.UpdateDepartureUseCase;
import swyp.mingling.global.documentation.MeetingApiDocumentation;
import swyp.mingling.global.response.ApiResponse;

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
    private final GetMeetingStatusUseCase getMeetingStatusUseCase;
    private final RecommendPlaceUseCase recommendPlaceUseCase;

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
                GetMidpointResponse.MidpointDto.builder()
                        .name("합정역")
                        .latitude(37.5484757)
                        .longitude(126.912071)
                        .avgTravelTime(30)
                        .transferPath("2호선 > 6호선")
                        .build(),
                GetMidpointResponse.MidpointDto.builder()
                        .name("서울역")
                        .latitude(37.554648)
                        .longitude(126.972559)
                        .avgTravelTime(35)
                        .transferPath("1호선 > 4호선")
                        .build(),
                GetMidpointResponse.MidpointDto.builder()
                        .name("용산역")
                        .latitude(37.529844)
                        .longitude(126.964804)
                        .avgTravelTime(32)
                        .transferPath("경의중앙선 > 1호선")
                        .build()
        );

        List<GetMidpointResponse.ParticipantPath> mockParticipantPaths = List.of(
                GetMidpointResponse.ParticipantPath.builder()
                        .userName("사용자A")
                        .departureStation("구로디지털단지역")
                        .transferPath("2호선 > 6호선")
                        .travelTime(25)
                        .stationNames(List.of("구로디지털단지", "신도림", "영등포구청", "당산", "합정"))
                        .build(),
                GetMidpointResponse.ParticipantPath.builder()
                        .userName("사용자B")
                        .departureStation("강남역")
                        .transferPath("2호선 > 6호선")
                        .travelTime(20)
                        .stationNames(List.of("강남", "역삼", "선릉", "삼성", "종합운동장", "합정"))
                        .build(),
                GetMidpointResponse.ParticipantPath.builder()
                        .userName("사용자C")
                        .departureStation("신림역")
                        .transferPath("2호선")
                        .travelTime(35)
                        .stationNames(List.of("신림", "봉천", "서울대입구", "낙성대", "사당", "방배", "서초", "교대", "강남", "역삼", "선릉"))
                        .build()
        );

        GetMidpointResponse response = GetMidpointResponse.builder()
                .midpoints(mockMidpoints)
                .participantPaths(mockParticipantPaths)
                .build();

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
                                                       @RequestParam(defaultValue = "10") int size) {
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
}

