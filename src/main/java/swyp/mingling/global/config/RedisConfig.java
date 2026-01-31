package swyp.mingling.global.config;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 장소 추천 캐시 전용 CacheManager
     * Key: String
     * Value: JSON 직렬화
     * TTL: 1일
     *
     * @param redisConnectionFactory Redis 연결 팩토리
     * @return 장소 추천 캐시를 위한 CacheManager
     */
    @Bean
    public CacheManager placeCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
            .defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    // Spring Data Redis 4.x에서 deprecated 되었으나 캐시 객체의 타입 안정성을 위해 사용
                    new GenericJackson2JsonRedisSerializer()
                )
            )
            .entryTtl(Duration.ofDays(1));

        return RedisCacheManager
            .RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .build();
    }
}
