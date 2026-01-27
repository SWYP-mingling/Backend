package swyp.mingling.domain.participant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.domain.participant.dto.StationCoordinate;
import swyp.mingling.domain.participant.dto.request.CreateDepartureRequest;
import swyp.mingling.domain.participant.dto.response.CreateDepartureResponse;
import swyp.mingling.domain.participant.repository.ParticipantRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateDepartureUseCase {

    private final ParticipantRepository participantRepository;
    private final MeetingRepository meetingRepository;
    private final FindStationCoordinateUseCase findStationCoordinateUseCase;

    public CreateDepartureResponse execute(UUID meetingId, CreateDepartureRequest request) {

//        // MEETING_NOT_FOUND
//        Meeting meeting = meetingRepository.findById(meetingId)
//                .orElseThrow(BusinessException::meetingNotFound);
//
//        // MEETING_CLOSED
//        if (meeting.getStatus().equals("closed")) {
//            throw BusinessException.meetingClosed();
//        }

        //닉네임 검증

        //지하철 역 위경도 추출
        StationCoordinate coordinate = findStationCoordinateUseCase.excute(request.getDeparture());

        return CreateDepartureResponse.of(
                request.getNickname(),
                request.getDeparture(),
                coordinate.getLatitude(),
                coordinate.getLongitude()

        );
    }
}
