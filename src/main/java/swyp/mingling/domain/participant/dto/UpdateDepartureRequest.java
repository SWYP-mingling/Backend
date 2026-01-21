package swyp.mingling.domain.participant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateDepartureRequest {

    @Schema(description = "모임 UUID", example = "합정역")
    private String departureName;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;
}
