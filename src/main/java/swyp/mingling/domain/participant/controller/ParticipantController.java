package swyp.mingling.domain.participant.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import swyp.mingling.domain.participant.dto.request.CreateDepartureRequest;
import swyp.mingling.domain.participant.dto.response.CreateDepartureResponse;
import swyp.mingling.domain.participant.dto.UpdateDepartureRequest;
import swyp.mingling.domain.participant.dto.UpdateDepartureResponse;
import swyp.mingling.global.documentation.ParticipantApiDocumentation;
import swyp.mingling.global.exception.BusinessException;
import swyp.mingling.global.response.ApiResponse;

import java.util.UUID;

@Tag(name = "참여자 API", description = "참여자 출발자 입력 및 관리 API")
@RestController
@RequestMapping("/participant")
public class ParticipantController {

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
        @Valid @RequestBody CreateDepartureRequest request) {

        // MEETING_NOT_FOUND
        if (meetingId.toString().equals("00000000-0000-0000-0000-000000000404")) {
            throw BusinessException.meetingNotFound();
        }

        // MEETING_CLOSED
        if (meetingId.toString().equals("00000000-0000-0000-0000-000000000409")) {
            throw BusinessException.meetingClosed();
        }

        // USER_UNAUTHORIZED
        if (request.getPassword().equals("unauthorized")) {
            throw BusinessException.userUnauthorized();
        }

        // STATION_NOT_FOUND
        if (request.getStationName().equals("stationNotFount")) {
            throw BusinessException.stationNotFound();
        }

        CreateDepartureResponse response = CreateDepartureResponse.of(
            request.getUserName(),
            request.getStationName(),
            request.getLatitude(),
            request.getLongitude()
        );

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
    public ApiResponse<UpdateDepartureResponse> updateDeparture(@PathVariable("meetingId") UUID meetingId,  @Valid @RequestBody UpdateDepartureRequest request) {

        Double mockLatitude = 37.497942;
        Double mockLongitude = 127.027621;

        return ApiResponse.success(new UpdateDepartureResponse(request.getUserName(), request.getDepartureName(), mockLatitude, mockLongitude));

    }

}
