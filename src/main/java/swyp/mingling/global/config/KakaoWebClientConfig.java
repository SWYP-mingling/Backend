package swyp.mingling.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 카카오 API 호출을 위한 WebClient 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
public class KakaoWebClientConfig {

    /**
     * 카카오 API Base URL
     */
    @Value("${kakao.local.base-url}")
    private String baseUrl;

    /**
     * 카카오 REST API Key
     */
    @Value("${kakao.rest-api-key}")
    private String restApiKey;

    /**
     * 카카오 API 전용 WebClient Bean
     *
     * @return 카카오 API 요청에 사용되는 WebClient
     */
    @Bean
    public WebClient kakaoWebClient() {
        return WebClient.builder().baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + restApiKey).build();
    }
}
