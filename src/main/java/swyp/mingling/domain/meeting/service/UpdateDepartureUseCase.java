package swyp.mingling.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.domain.meeting.dto.StationCoordinate;
import swyp.mingling.domain.meeting.dto.request.UpdateDepartureRequest;
import swyp.mingling.domain.meeting.dto.response.UpdateDepartureResponse;
import swyp.mingling.domain.participant.entity.Participant;
import swyp.mingling.domain.participant.repository.ParticipantRepository;
import swyp.mingling.global.exception.BusinessException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateDepartureUseCase {

    private final ParticipantRepository participantRepository;
    private final MeetingRepository meetingRepository;
    private final FindStationCoordinateUseCase findStationCoordinateUseCase;

    @Transactional
    public UpdateDepartureResponse execute(UUID meetingId, String nickname, UpdateDepartureRequest request) {

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

        //지하철 역 위경도 추출
        StationCoordinate coordinate = findStationCoordinateUseCase.excute(request.getDeparture());

        //DB저장
        participant.updateDeparture(request.getDeparture());

        return UpdateDepartureResponse.of(
                nickname,
                request.getDeparture(),
                coordinate.getLatitude(),
                coordinate.getLongitude()
        );
    }
}
