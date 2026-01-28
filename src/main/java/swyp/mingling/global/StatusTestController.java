package swyp.mingling.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StatusTestController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/api/status")
    public Map<String, Object> checkStatus() {
        Map<String, Object> status = new HashMap<>();

        // 1. DB 연결 체크
        try (Connection conn = dataSource.getConnection()) {
            status.put("database", "Connected (MariaDB)");
        } catch (Exception e) {
            status.put("database", "Disconnected: " + e.getMessage());
        }

        // 2. Redis 연결 체크
        try {
            redisTemplate.opsForValue().set("status_check", "ok");
            status.put("redis", "Connected (Redis)");
        } catch (Exception e) {
            status.put("redis", "Disconnected: " + e.getMessage());
        }

        status.put("server_time", java.time.LocalDateTime.now().toString());
        return status;
    }
}

