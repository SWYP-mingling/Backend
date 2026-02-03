package swyp.mingling.domain.meeting.dto.response.midpoint;


import lombok.Builder;
import lombok.Getter;
import swyp.mingling.domain.meeting.entity.HotPlace;

@Getter
@Builder
public class HotPlaceCategoryResponse {

    private String name;
    private double latitude;
    private double longitude;

    public static HotPlaceCategoryResponse from(HotPlace hotPlace) {
        return HotPlaceCategoryResponse.builder()
                .name(hotPlace.getName())
                .latitude(hotPlace.getLatitude())
                .longitude(hotPlace.getLongitude())
                .build();
    }
}
