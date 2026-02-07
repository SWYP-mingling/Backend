package swyp.mingling.domain.meeting.dto.response.midpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StationPathResponse {

    @Schema(description = "지하철역 호선", example = "2호선")
    private String linenumber;

    @Schema(description = "지하철역", example = "홍대입구역")
    private String station;

    @Schema(description = "위도", example = "37.5567")
    private double latitude;

    @Schema(description = "경도", example = "126.9236")
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
