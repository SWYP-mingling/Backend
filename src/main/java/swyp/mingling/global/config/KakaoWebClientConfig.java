package swyp.mingling.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class KakaoWebClientConfig {

    @Value("${kakao.local.base-url}")
    private String baseUrl;

    @Value("${kakao.rest-api-key:}")
    private String restApiKey;

    @Bean
    public WebClient kakaoWebClient() {
        return WebClient.builder().baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + restApiKey).build();
    }
}
