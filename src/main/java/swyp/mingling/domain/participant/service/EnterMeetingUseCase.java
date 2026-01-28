package swyp.mingling.domain.participant.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.domain.participant.dto.request.EnterMeetingRequest;
import swyp.mingling.domain.participant.repository.ParticipantRepository;
import swyp.mingling.global.exception.BusinessException;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
@EnableRedisHttpSession
public class EnterMeetingUseCase {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Transactional
    public void execute(UUID meetingId,
                                       EnterMeetingRequest request,
                                       HttpServletRequest httprequest) {


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

        // 세션 추가
        HttpSession session = httprequest.getSession(true);
        String sessionId = session.getId();
        log.info(sessionId);


        session.setAttribute(String.valueOf(meetingId), request.getUserId());
        session.setMaxInactiveInterval(60 * 60 * 24 * 30);
//        redisTemplate.delete("spring:session:sessions:" + sessionId);

        // 쿠키 추가
        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        cookie.setPath("/meeting/" + meeting.getInviteUrl());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30일

    }
}
