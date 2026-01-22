package swyp.mingling.domain.meeting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetMidpointRequest {

    @Schema(description = "사용자 이름", example = "스위프")
    private String userId;

}
