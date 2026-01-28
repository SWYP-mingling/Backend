package swyp.mingling.global.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import swyp.mingling.global.exception.BusinessException;

import java.util.Map;



@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // meetingid 가져오기
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String meetingId = pathVariables.get("meetingId");

        // 쿠키 nickname 가져오기
        Cookie[] cookies = request.getCookies();
        String nickname = null;
        String sessionId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("nickname".equals(cookie.getName())) nickname = cookie.getValue();
                if ("fakeSessionId".equals(cookie.getName())) sessionId = cookie.getValue();
            }
        }

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

            if (sessionId != null) {

                // 쿠키가 살아 있을때

                // 죽은 세션 삭제
                redisTemplate.delete("spring:session:sessions:" + sessionId);

                // session 재생성
                HttpSession newsession = request.getSession();
                newsession.setAttribute(String.valueOf(meetingId), nickname);
                newsession.setMaxInactiveInterval(60 * 60 * 24 * 7);

                return true;

            }

            // 쿠키가 만료 되었을때

            // 에러
            throw BusinessException.sessionerror();
        }

    }
}
