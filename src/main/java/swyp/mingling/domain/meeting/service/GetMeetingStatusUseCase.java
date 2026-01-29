package swyp.mingling.domain.meeting.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.mingling.domain.meeting.dto.StationCoordinate;
import swyp.mingling.domain.meeting.dto.response.GetMeetingStatusResponse;
import swyp.mingling.domain.meeting.dto.response.GetMeetingStatusResponse.ParticipantInfo;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.meeting.repository.MeetingQueryRepository;
import swyp.mingling.global.exception.BusinessException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMeetingStatusUseCase {

    private final MeetingQueryRepository meetingQueryRepository;
    private final FindStationCoordinateUseCase findStationCoordinateUseCase;

    public GetMeetingStatusResponse execute(UUID meetingId) {
        // 1. 데이터 조회
        Meeting meeting = meetingQueryRepository.findMeetingStatusById(meetingId)
            .orElseThrow(() -> BusinessException.meetingNotFound());

        // 2. DTO 변환
        List<ParticipantInfo> participantInfos = meeting.getParticipants().stream()
            .map(p -> {
                StationCoordinate coordinate = findStationCoordinateUseCase.excute(p.getDeparture());

                return GetMeetingStatusResponse.ParticipantInfo.of(
                    p.getNickname(),
                    p.getDeparture(),
                    coordinate.getLatitude(),
                    coordinate.getLongitude()
                );
            })
            .toList();

        return GetMeetingStatusResponse.of(
            meeting.getCount(),
            participantInfos.size(),
            meeting.getDeadline(),
            participantInfos
        );
    }


}
