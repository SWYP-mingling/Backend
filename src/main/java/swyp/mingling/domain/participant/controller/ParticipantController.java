package swyp.mingling.domain.participant.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import swyp.mingling.domain.participant.dto.UpdateDepartureRequest;
import swyp.mingling.domain.participant.dto.UpdateDepartureResponse;
import swyp.mingling.global.documentation.ParticipantApiDocumentation;
import swyp.mingling.global.response.ApiResponse;

import java.util.UUID;

@Tag(name = "참여자 API", description = "참여자 출발자 입력 및 관리 API")
@RestController
@RequestMapping("/participant")
public class ParticipantController {
    /**
     * 출발역 수정 API
     *
     * @param request 출발역 수정 요청 DTO
     * @return 사용자정보, 출발역 정보
     */
    @ParticipantApiDocumentation.UpdateDepartDoc
    @PatchMapping("/{meetingId}/departure")
    public ApiResponse<UpdateDepartureResponse> updateDeparture(@PathVariable("meetingId") UUID meetingId,  @Valid @RequestBody UpdateDepartureRequest request) {

        Double mockLatitude = 37.497942;
        Double mockLongitude = 127.027621;

        return ApiResponse.success(new UpdateDepartureResponse(request.getUserName(), request.getDepartureName(), mockLatitude, mockLongitude));
    }

}
