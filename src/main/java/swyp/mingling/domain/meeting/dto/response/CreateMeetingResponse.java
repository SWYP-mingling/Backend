package swyp.mingling.domain.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 모임 생성 응답 DTO
 */
@Getter
@AllArgsConstructor
public class CreateMeetingResponse {

    @Schema(description = "모임 URL", example = "https://mingling.com/meeting/abc123def456")
    private String meetingUrl;

    @Schema(description = "모임 UUID", example = "abc123def456")
    private String meetingId;
}
