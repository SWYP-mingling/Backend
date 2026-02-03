package swyp.mingling.domain.meeting.dto.response.midpoint;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransferPathResponse {
    private String lineName;
    private String stationName;
    private double latitude;
    private double longitude;

    public static StationPathResponse from(String lineName, String stationName, double latitude, double longitude) {
        return StationPathResponse.builder()
                .linenumber(lineName)
                .station(stationName)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
