package swyp.mingling.domain.participant.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swyp.mingling.domain.participant.dto.request.EnterMeetingRequest;
import swyp.mingling.domain.participant.service.EnterMeetingUseCase;
import swyp.mingling.global.documentation.ParticipantApiDocumentation;
import swyp.mingling.global.response.ApiResponse;

import java.util.UUID;

@Tag(name = "참여자 API", description = "참여자 출발자 입력 및 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/participant")
public class ParticipantController {

    private final EnterMeetingUseCase enterMeetingUseCase;

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
