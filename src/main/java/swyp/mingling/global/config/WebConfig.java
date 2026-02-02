package swyp.mingling.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import swyp.mingling.global.interceptor.LoginCheckInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor interceptor;

    @Value("${mingling.api.test-api}")
    private String testapi;

    @Value("${mingling.api.prod-api}")
    private String prodapi;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).excludePathPatterns("/participant/**/enter",
                                                                "/api/meeting",
                                                                "/meeting",
                                                                "/swagger-resources/**",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs",
                                                                "/api-docs/**",
                                                                "/api/status",
                                                                "/actuator/health",
                                                                "/meeting/result/**");

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://mingling.kr", "http://localhost:3000","https://www.mingling.kr",testapi, prodapi)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}