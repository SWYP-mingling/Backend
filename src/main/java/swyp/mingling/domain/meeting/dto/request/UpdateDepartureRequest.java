package swyp.mingling.domain.meeting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateDepartureRequest {

    @Schema(description = "중간지점 이름", example = "합정역")
    private String departure;
}
