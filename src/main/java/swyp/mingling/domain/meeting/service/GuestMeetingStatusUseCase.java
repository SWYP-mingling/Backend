package swyp.mingling.domain.meeting.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swyp.mingling.domain.meeting.dto.response.GuestStatusResponse;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.meeting.repository.MeetingQueryRepository;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.domain.participant.entity.Participant;
import swyp.mingling.global.exception.BusinessException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestMeetingStatusUseCase {

    private final MeetingQueryRepository meetingQueryRepository;
    private final MeetingRepository meetingRepository;

    public GuestStatusResponse execute(UUID meetingId) {

        // 1. 데이터 조회
        Meeting meeting = meetingQueryRepository.findMeetingStatusById(meetingId)
                .orElseThrow(() -> BusinessException.meetingNotFound());

        // 2. 출발지 설정된 참여자
        List<Participant> confirmedParticipants = meeting.getParticipants().stream()
                .filter(p -> p.getDeparture() != null)
                .toList();

        // 3. DTO 변환
        List<GuestStatusResponse.ParticipantInfo> participantInfos =
                confirmedParticipants.stream()
                        .map(p -> GuestStatusResponse.ParticipantInfo.of(
                                p.getNickname()
                        ))
                        .toList();

        String category = meetingRepository.findPurposeNamesByMeetingId(meetingId).orElse("식당");

        return GuestStatusResponse.of(
                meeting.getName(),
                category,
                meeting.getCount(),
                participantInfos.size(),
                participantInfos
        );

    }
}
