package swyp.mingling.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import swyp.mingling.external.dto.response.SeoulMetroRouteResponse;

@Slf4j
@Component
public class SeoulMetroClient {

    private final WebClient webClient;

    public SeoulMetroClient(@Qualifier("seoulMetroWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public SeoulMetroRouteResponse searchRoute(String startStationName, String endStationName) {

        String start = startStationName;
        String end = endStationName;

        if(start != null && !"서울역".equals(start)){
            start = start.replaceAll("역$", "");
        }

        if(end != null && !"서울역".equals(end)){
            end = end.replaceAll("역$", "");
        }

        String finalStart = start;
        String finalEnd = end;

//        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 지하철 운행시간 종료 후
        String now = "2026-06-06 14:00:00";
        return webClient.get()
                .uri(uriBuilder -> {
                    var uri = uriBuilder
                            .pathSegment("getShtrmPath", "1", "5", finalStart, finalEnd, now)
                            .queryParam("schInclYn", "N")
                            .build();

                    log.info("최종 API 요청 URL: {}", uri);
                    return uri;
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        status -> status.is5xxServerError(),
                        clientResponse -> {
                            log.error("공공 데이터 API 서버 오류 발생 - HTTP 상태: {}", clientResponse.statusCode());
                            return clientResponse.bodyToMono(String.class)
                                    .doOnNext(body -> log.error("API 에러 응답 body: {}", body))
                                    .then(Mono.error(new RuntimeException("공공 데이터 포털 API 서버 오류")));
                        }
                )
                .bodyToMono(String.class)
                .doOnNext(rawBody -> log.info("API Raw 응답 body: {}", rawBody))
                .flatMap(rawBody -> {
                    try {
                        // JSON 파싱 시도
                        return Mono.just(new com.fasterxml.jackson.databind.ObjectMapper()
                                .readValue(rawBody, SeoulMetroRouteResponse.class));
                    } catch (Exception e) {
                        log.error("JSON 파싱 실패: {}", e.getMessage());
                        return Mono.just(new SeoulMetroRouteResponse());
                    }
                })
                .doOnSuccess(response -> {
                    if (response != null) {
                        log.info("API 응답 받음 - header: {}, body exists: {}",
                                response.getHeader(), response.getBody() != null);
                    } else {
                        log.warn("API 응답이 null입니다");
                    }
                })
                .doOnError(error -> log.error("WebClient 에러 발생: {}", error.getMessage(), error))
                .block();
    }
}