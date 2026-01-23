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
    private String userName;

    @NotBlank
    @Schema(description = "참여자 비밀번호", example = "Password1!")
    private String password;

    @NotBlank
    @Schema(description = "출발역 이름", example = "구로디지털단지역")
    private String stationName;

    @Schema(description = "위도", example = "37.485266")
    private Double latitude;

    @Schema(description = "경도", example = "126.901401")
    private Double longitude;

}
