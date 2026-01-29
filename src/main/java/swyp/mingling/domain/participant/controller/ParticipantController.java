package swyp.mingling.domain.participant.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swyp.mingling.domain.participant.dto.request.CreateDepartureRequest;
import swyp.mingling.domain.participant.dto.request.EnterMeetingRequest;
import swyp.mingling.domain.participant.dto.request.UpdateDepartureRequest;
import swyp.mingling.domain.participant.dto.response.CreateDepartureResponse;
import swyp.mingling.domain.participant.dto.response.UpdateDepartureResponse;
import swyp.mingling.domain.participant.service.CreateDepartureUseCase;
import swyp.mingling.domain.participant.service.EnterMeetingUseCase;
import swyp.mingling.domain.participant.service.UpdateDepartureUseCase;
import swyp.mingling.global.documentation.ParticipantApiDocumentation;
import swyp.mingling.global.response.ApiResponse;

import java.util.UUID;

@Tag(name = "참여자 API", description = "참여자 출발자 입력 및 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/participant")
public class ParticipantController {

    private final EnterMeetingUseCase enterMeetingUseCase;
    private final CreateDepartureUseCase createDepartureUseCase;
    private final UpdateDepartureUseCase updateDepartureUseCase;

    /**
     * 출발역 등록 API
     *
     * @param meetingId 모임 UUID
     * @param request 출발역 등록 요청 DTO
     * @return 출발역 등록 응답 DTO
     */
    @ParticipantApiDocumentation.CreateDepartureDoc
    @PostMapping("/{meetingId}/departure")
    public ApiResponse<CreateDepartureResponse> createDeparture(
        @PathVariable("meetingId") UUID meetingId,
        @Parameter(hidden = true) @SessionAttribute(name = "nickname", required = true) String nickname,
        @Valid @RequestBody CreateDepartureRequest request) {

        CreateDepartureResponse response = createDepartureUseCase.execute(meetingId, nickname, request);

        return ApiResponse.success(response);
    }

    /**
     * 출발역 수정 API
     *
     * @param request 출발역 수정 요청 DTO
     * @return 사용자정보, 출발역 정보
     */
    @ParticipantApiDocumentation.UpdateDepartDoc
    @PatchMapping("/{meetingId}/departure")
    public ApiResponse<UpdateDepartureResponse> updateDeparture(
            @PathVariable("meetingId") UUID meetingId,
            @Parameter(hidden = true) @SessionAttribute(name = "nickname", required = true) String nickname,
            @Valid @RequestBody UpdateDepartureRequest request) {

        UpdateDepartureResponse response = updateDepartureUseCase.execute(meetingId, nickname, request);

        return ApiResponse.success(response);

    }


    /**
     * 모임 입장하기 API
     *
     * @param meetingId 모임 UUID
     * @param request   입장 요청 DTO (이름, 비밀번호)
     * @return JWT 토큰
     */
    @ParticipantApiDocumentation.EnterMeetingDoc
    @PostMapping("/{meetingId}/enter")
    public ApiResponse<Object> enterMeeting(
            @PathVariable("meetingId") UUID meetingId,
            @Valid @RequestBody EnterMeetingRequest request,
            HttpServletRequest httprequest,
            HttpServletResponse httpresponse) {

        enterMeetingUseCase.execute(meetingId, request, httprequest, httpresponse);

        return ApiResponse.success();

    }
}
