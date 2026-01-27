package swyp.mingling.domain.participant.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import swyp.mingling.domain.participant.dto.StationCoordinate;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FindStationCoordinateUseCase {

    private final ObjectMapper objectMapper;
    private final Map<String, StationCoordinate> coordinateCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {

            InputStream is = new ClassPathResource("stations_info.json").getInputStream();

            List<Map<String, Object>> rawData = objectMapper.readValue(is, new TypeReference<>() {});

            for (Map<String, Object> data : rawData) {
                String name = (String) data.get("name");
                Double lat = (Double) data.get("latitude");
                Double lng = (Double) data.get("longitude");

                if (name != null && lat != null && lng != null) {
                    coordinateCache.put(name, StationCoordinate.of(lat, lng));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("지하철역 좌표 데이터를 로드하는 데 실패했습니다.", e);
        }
    }

    public StationCoordinate excute(String departure) {

        String cleanName = departure.endsWith("역")
                ? departure.substring(0, departure.length() - 1)
                : departure;

        StationCoordinate coordinate = coordinateCache.get(cleanName);

        if (coordinate == null) {
            coordinate = coordinateCache.get(departure);
        }

        if (coordinate == null) {
            throw new RuntimeException("해당 역의 좌표를 찾을 수 없습니다: " + departure);
        }

        return StationCoordinate.of(
                coordinate.getLatitude(),
                coordinate.getLongitude()
        );
    }
}
