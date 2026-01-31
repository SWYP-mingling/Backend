package swyp.mingling.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 서울시 지하철 API 호출을 위한 WebClient 설정 클래스
 */
@Configuration
public class SeoulMetroWebClientConfig {

    /**
     * 서울 열린데이터광장 API Base URL
     */
    @Value("${seoul.metro.base-url}")
    private String baseUrl;

    /**
     * 서울 열린데이터광장 API 인증키
     */
    @Value("${seoul.metro.api-key}")
    private String apiKey;

    /**
     * 서울시 지하철 API 전용 WebClient Bean
     *
     * @return 서울시 지하철 API 요청에 사용되는 WebClient
     */
    @Bean
    public WebClient seoulMetroWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl + "/" + apiKey + "/json")
                .build();
    }
}
