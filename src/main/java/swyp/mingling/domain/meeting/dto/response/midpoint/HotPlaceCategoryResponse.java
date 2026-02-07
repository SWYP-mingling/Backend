package swyp.mingling.domain.meeting.dto.response.midpoint;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import swyp.mingling.domain.meeting.entity.HotPlace;

@Getter
@Builder
public class HotPlaceCategoryResponse {

    @Schema(description = "번화가 이름", example = "홍대입구")
    private String name;

    @Schema(description = "위도", example = "37.5567")
    private double latitude;

    @Schema(description = "경도", example = "126.9236")
    private double longitude;

    private String line;

    public static HotPlaceCategoryResponse from(HotPlace hotPlace) {
        return HotPlaceCategoryResponse.builder()
                .name(hotPlace.getName())
                .latitude(hotPlace.getLatitude())
                .longitude(hotPlace.getLongitude())
                .line(hotPlace.getLine())
                .build();
    }
}
