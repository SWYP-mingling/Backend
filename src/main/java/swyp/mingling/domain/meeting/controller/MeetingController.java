package swyp.mingling.domain.meeting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.mingling.domain.meeting.dto.CreateMeetingRequest;
import swyp.mingling.domain.meeting.dto.CreateMeetingResponse;
import swyp.mingling.global.documentation.MeetingApiDocumentation;
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
}

