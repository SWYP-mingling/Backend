package swyp.mingling.domain.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StationCoordinate {
    private Double latitude;
    private Double longitude;

    public static StationCoordinate of(
            Double latitude,
            Double longitude
    ) {
        return new StationCoordinate(latitude, longitude);
    }
}
