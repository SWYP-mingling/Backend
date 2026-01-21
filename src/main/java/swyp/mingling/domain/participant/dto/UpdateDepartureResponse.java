package swyp.mingling.domain.participant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateDepartureResponse {

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "출발지 이룸", example = "강남역")
    private String departureName;

    @Schema(description = "출발지 위도", example = "37.497942")
    private Double latitude;

    @Schema(description = "출발지 경도", example = "127.027621")
    private Double longitude;

}
