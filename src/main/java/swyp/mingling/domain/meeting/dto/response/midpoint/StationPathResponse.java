package swyp.mingling.domain.meeting.dto.response.midpoint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StationPathResponse {

    private String linenumber;
    private String station;
    private double latitude;
    private double longitude;


    public static StationPathResponse from(String linenumber, String station, double latitude, double longitude) {
        return StationPathResponse.builder()
                .linenumber(linenumber)
                .station(station)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }



}
