package swyp.mingling.domain.participant.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@EnableRedisHttpSession
public class EnterMeetingUseCase {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;


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
            participantRepository.save(request.toEntity(meeting));
        }

        String encodedNickname =
                URLEncoder.encode(request.getUserId(), StandardCharsets.UTF_8);

        // 세션 추가
        HttpSession session = httprequest.getSession(true);
        String sessionId = session.getId();
        session.setAttribute(String.valueOf(meetingId), request.getUserId());
        session.setMaxInactiveInterval(60 * 60 * 24 * 7); // 7일

        // 가짜 세션 쿠키 추가
        ResponseCookie fakeSessionCookie = ResponseCookie.from("fakeSessionId", sessionId)
                .path("/api/meeting/" + meetingId)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 7)     // 7일
                .build();

        // username 쿠키 추가
        ResponseCookie nicknameCookie = ResponseCookie.from("nickname", encodedNickname)
                .path("/api/meeting/" + meetingId)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 7)
                .build();

        httpresponse.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, fakeSessionCookie.toString());
        httpresponse.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, nicknameCookie.toString());

    }
}
