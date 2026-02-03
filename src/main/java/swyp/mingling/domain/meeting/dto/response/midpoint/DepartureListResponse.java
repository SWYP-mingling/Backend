package swyp.mingling.domain.meeting.dto.response.midpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartureListResponse {

    @Schema(description = "사용자 닉네임", example = "nick")
    private String nickname;

    @Schema(description = "사용자 출발지", example = "홍대입구역")
    private String departure;
}
