package swyp.mingling.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import swyp.mingling.external.dto.response.SeoulMetroRouteResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class SeoulMetroClient {

    private final WebClient webClient;

    public SeoulMetroClient(@Qualifier("seoulMetroWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public SeoulMetroRouteResponse searchRoute(String startStationName, String endStationName) {
        String start = startStationName.replaceAll("역$", "");
        String end = endStationName.replaceAll("역$", "");
        return webClient.get()
                .uri(uriBuilder -> {
                    var uri = uriBuilder
                            .pathSegment("getShtrmPath", "1", "5", start, end, now)
                            .queryParam("schInclYn", "N")
                            .build();

                    log.info("최종 API 요청 URL: {}", uri);
                    return uri;
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SeoulMetroRouteResponse.class)
                .block();
    }
}