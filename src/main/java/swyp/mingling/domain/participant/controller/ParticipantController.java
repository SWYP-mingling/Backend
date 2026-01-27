package swyp.mingling.domain.participant.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swyp.mingling.domain.participant.dto.request.CreateDepartureRequest;
import swyp.mingling.domain.participant.dto.request.UpdateDepartureRequest;
import swyp.mingling.domain.participant.dto.response.CreateDepartureResponse;
import swyp.mingling.domain.participant.dto.response.UpdateDepartureResponse;
import swyp.mingling.domain.participant.service.CreateDepartureUseCase;
import swyp.mingling.global.documentation.ParticipantApiDocumentation;
import swyp.mingling.global.response.ApiResponse;

import java.util.UUID;

@Tag(name = "참여자 API", description = "참여자 출발자 입력 및 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/participant")
public class ParticipantController {

    private final CreateDepartureUseCase createDepartureUseCase;

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
        @SessionAttribute(name = "nickname", required = true) String nickname,
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
    public ApiResponse<UpdateDepartureResponse> updateDeparture(@PathVariable("meetingId") UUID meetingId,
                                                                @Parameter(hidden = true)
                                                                @SessionAttribute(name = "userName", required = false) String userName,
                                                                @Valid @RequestBody UpdateDepartureRequest request) {

        Double mockLatitude = 37.497942;
        Double mockLongitude = 127.027621;

        return ApiResponse.success(new UpdateDepartureResponse(userName, request.getDepartureName(), mockLatitude, mockLongitude));

    }

}
