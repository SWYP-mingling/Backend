package swyp.mingling.global.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import swyp.mingling.global.exception.BusinessException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${mingling.cookie.path}")
    private String cookiePath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // meetingid 가져오기
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        // meetingId가 없는 경로(예: POST /meeting 생성)라면 검증을 생략하고 통과 - 방어로직
        if (pathVariables == null || !pathVariables.containsKey("meetingId")) {
            log.info("meetingId가 없는 경로이므로 인터셉터를 통과합니다. URI: {}", request.getRequestURI());
            return true;
        }

        String meetingId = pathVariables.get("meetingId");

        if(meetingId == null) {
            throw BusinessException.meetingNotFound();
        }

        // 쿠키 nickname, sessionid 가져오기
        Cookie[] cookies = request.getCookies();
        String nickname = null;
        String sessionId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("nickname".equals(cookie.getName())) nickname = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                if ("fakeSessionId".equals(cookie.getName())) sessionId = cookie.getValue();
            }
        }

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        sessionId = ops.get(sessionId);

        HttpSession session = request.getSession(false);

        if(session != null) {

            // 세션이 있을때
            String sessionNickname = (String) session.getAttribute(meetingId);


            if (sessionNickname == null || !sessionNickname.equals(nickname)) {
                // nickname이 존재하지 않거나 meetingId가 없을때

                // 세션 삭제
                session.invalidate();

                // 에러
                throw BusinessException.sessionerror();
            }

            // nickname과 meetingId가 존재할 때
            return true;

        } else {

            // 세션이 없을때

            if (nickname != null) {

                // 쿠키가 살아 있을때

                // 죽은 세션 삭제
                if (sessionId != null) {
                    redisTemplate.delete("spring:session:sessions:" + sessionId);
                }

                // session 재생성
                HttpSession newsession = request.getSession();
                String newsessionId = newsession.getId();
                newsession.setAttribute(String.valueOf(meetingId), nickname);
                newsession.setMaxInactiveInterval(60 * 60); // 1시간


                // fakeSessionId 값 갱신
                String randomValue = UUID.randomUUID().toString();

                ValueOperations<String, String> ops1 = redisTemplate.opsForValue();
                ops1.set(randomValue, newsessionId, 1, TimeUnit.HOURS);

                ResponseCookie fakeSessionCookie = ResponseCookie.from("fakeSessionId", randomValue)
                        .path(cookiePath + meetingId)
                        .sameSite("None")
                        .secure(true)
                        .httpOnly(true)
                        .maxAge(60 * 60) // 1시간
                        .build();

                response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, fakeSessionCookie.toString());

                return true;

            }

            // 쿠키가 만료 되었을때

            // 에러
            throw BusinessException.sessionerror();
        }

    }
}
