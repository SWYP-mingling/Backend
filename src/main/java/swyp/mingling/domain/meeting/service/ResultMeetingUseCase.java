package swyp.mingling.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.mingling.domain.meeting.dto.response.ResultMeetingResponse;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.global.exception.BusinessException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultMeetingUseCase {

    private final MeetingRepository meetingRepository;

    public ResultMeetingResponse getResultMeetingUrl(String meetingId) {
        // String을 UUID로 변환
        UUID uuid = UUID.fromString(meetingId);

        // 모임 존재 여부 확인
        Meeting meeting = meetingRepository.findById(uuid)
                .orElseThrow(BusinessException::meetingNotFound);

        // 공유 URL 생성
        String meetingUrl = "https://mingling.com/meeting/" + meeting.getId();

        return new ResultMeetingResponse(meetingUrl);
    }
}
