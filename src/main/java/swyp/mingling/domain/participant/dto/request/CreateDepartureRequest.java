package swyp.mingling.domain.participant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 출발역 등록 요청 DTO
 */
@Getter
@AllArgsConstructor
@Schema(description = "출발역 등록 요청 DTO")
public class CreateDepartureRequest {

    @NotBlank
    @Schema(description = "참여자 이름", example = "김밍글")
    private String nickname;

    @NotBlank
    @Schema(description = "출발역 이름", example = "구로디지털단지역")
    private String departure;

}
