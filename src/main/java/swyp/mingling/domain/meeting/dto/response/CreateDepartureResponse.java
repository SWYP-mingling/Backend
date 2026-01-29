package swyp.mingling.domain.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 출발역 등록 응답 DTO
 */
@Getter
@AllArgsConstructor
@Schema(description = "출발역 등록 응답 DTO")
public class CreateDepartureResponse {

    @Schema(description = "참여자 이름", example = "김밍글")
    private String nickname;

    @Schema(description = "출발역 이름", example = "구로디지털단지역")
    private String departure;

    @Schema(description = "위도", example = "37.485266")
    private Double latitude;

    @Schema(description = "경도", example = "126.901401")
    private Double longitude;

    /**
     * 정적 팩토리 메서드
     */
    public static CreateDepartureResponse of(
        String nickname,
        String departure,
        Double latitude,
        Double longitude
    ) {
        return new CreateDepartureResponse(nickname, departure, latitude, longitude);
    }
}
