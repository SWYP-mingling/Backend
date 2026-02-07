package swyp.mingling.domain.participant.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseCookie;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.domain.participant.dto.request.EnterMeetingRequest;
import swyp.mingling.domain.participant.repository.ParticipantRepository;
import swyp.mingling.global.exception.BusinessException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@EnableRedisHttpSession
public class EnterMeetingUseCase {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;

    @Value("${mingling.cookie.path}")
    private String cookiePath;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Transactional
    public void execute(UUID meetingId,
                        EnterMeetingRequest request,
                        HttpServletRequest httprequest,
                        HttpServletResponse httpresponse) {

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(BusinessException::meetingNotFound);

        boolean byNicknameAndPassword = participantRepository.existsByMeetingAndNicknameAndPassword(meeting, request.getUserId(), request.getPassword());
        boolean byNickname = participantRepository.existsByMeetingAndNickname(meeting, request.getUserId());

        if(byNickname) {
            if(!byNicknameAndPassword) {
                throw BusinessException.meetingUser();
            }
        } else {
            // 새로운 참여자인 경우, 실제 참여자 수 체크
            long currentParticipantCount = participantRepository.countByMeetingAndIsDeletedFalse(meeting);
            long maxParticipants = meeting.getCount();

            if(currentParticipantCount >= maxParticipants) { // 참여자 MAX 값
                throw BusinessException.capacityExceeded();
            }

            participantRepository.save(request.toEntity(meeting));
        }

        // 세션 추가
        HttpSession session = httprequest.getSession(true);
        String sessionId = session.getId();
        session.setAttribute(String.valueOf(meetingId), request.getUserId());
        session.setMaxInactiveInterval(60 * 60); // 1시간

        // 가짜 세션 쿠키 추가
        String randomValue = UUID.randomUUID().toString();

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(randomValue, sessionId, 1, TimeUnit.HOURS);

        ResponseCookie fakeSessionCookie = ResponseCookie.from("fakeSessionId", randomValue)
                .path(cookiePath + meetingId)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .maxAge(60 * 60) // 1시간
                .build();

        // username 쿠키 추가
        String encodedNickname =
                URLEncoder.encode(request.getUserId(), StandardCharsets.UTF_8);

        ResponseCookie nicknameCookie = ResponseCookie.from("nickname", encodedNickname)
                .path(cookiePath + meetingId)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 7)
                .build();

        httpresponse.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, fakeSessionCookie.toString());
        httpresponse.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, nicknameCookie.toString());

    }
}
