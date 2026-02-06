package swyp.mingling.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.domain.participant.entity.Participant;
import swyp.mingling.domain.participant.repository.ParticipantRepository;
import swyp.mingling.global.exception.BusinessException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteDepartureUseCase {

    private final ParticipantRepository participantRepository;
    private final MeetingRepository meetingRepository;
    private final FindStationCoordinateUseCase findStationCoordinateUseCase;

    @Transactional
    public String execute(UUID meetingId, String nickname) {

        // MEETING_NOT_FOUND
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(BusinessException::meetingNotFound);

        // MEETING_CLOSED
        if (meeting.getStatus().equals("closed")) {
            throw BusinessException.meetingClosed();
        }

        //사용자 검증
        Participant participant = participantRepository.findByMeetingIdAndNickname(meetingId, nickname)
                .orElseThrow(BusinessException::userUnauthorized);

        //모임 있나 검증
        if(participant.getDeparture() == null) {
            throw BusinessException.departureNotFound();
        }

        //DB저장
        participant.updateDeparture(null);

        return participant.getNickname();
    }
}
