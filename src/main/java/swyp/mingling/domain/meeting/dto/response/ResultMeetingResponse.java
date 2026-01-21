package swyp.mingling.domain.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결과 공유 응답 DTO
 */
@Getter
@AllArgsConstructor
public class ResultMeetingResponse {

    @Schema(description = "모임 URL 주소", example = "https://mingling.com/meeting/abc123def456")
    private String meetingUrl;
}
