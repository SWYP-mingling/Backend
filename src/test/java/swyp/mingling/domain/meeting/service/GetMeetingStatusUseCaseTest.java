package swyp.mingling.domain.meeting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import swyp.mingling.domain.meeting.dto.StationCoordinate;
import swyp.mingling.domain.meeting.dto.response.GetMeetingStatusResponse;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.meeting.repository.MeetingQueryRepository;
import swyp.mingling.domain.participant.entity.Participant;
import swyp.mingling.global.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class GetMeetingStatusUseCaseTest {

    @InjectMocks
    private GetMeetingStatusUseCase getMeetingStatusUseCase;

    @Mock
    private MeetingQueryRepository meetingQueryRepository;

    @Mock
    private FindStationCoordinateUseCase findStationCoordinateUseCase;

    /**
     * [성공 케이스]
     */
    @Test
    @DisplayName("모임 참여 현황을 정상적으로 조회한다.")
    public void success() {

        // given
        UUID meetingId = UUID.randomUUID();

        Participant p1 = mockParticipantWithDeparture("이대호", "구로디지털단지역");
        Participant p2 = mockParticipantWithDeparture("최동원", "합정역");
        Participant p3 = mockParticipantWithDeparture("손민한", "선릉역");

        Meeting meeting = mockMeeting(List.of(p1, p2, p3), 5, LocalDateTime.of(2026, 1, 30, 4, 0));

        given(meetingQueryRepository.findMeetingStatusById(meetingId))
            .willReturn(Optional.of(meeting));

        given(findStationCoordinateUseCase.excute(anyString()))
            .willReturn(StationCoordinate.of(37.0, 127.0));

        // when
        GetMeetingStatusResponse response = getMeetingStatusUseCase.execute(meetingId);

        // then
        assertThat(response.getTotalParticipantCount()).isEqualTo(5);
        assertThat(response.getCurrentParticipantCount()).isEqualTo(3);
        assertThat(response.getPendingParticipantCount()).isEqualTo(2);
        assertThat(response.getParticipants()).hasSize(3);

        verify(findStationCoordinateUseCase, times(3))
            .excute(anyString());
    }

    /**
     * [실패 케이스]
     */
    @Test
    @DisplayName("모임이 존재하지 않으면 예외를 던진다.")
    public void fail_whenMeetingNotFound() {

        // given
        UUID meetingId = UUID.randomUUID();

        given(meetingQueryRepository.findMeetingStatusById(meetingId))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> getMeetingStatusUseCase.execute(meetingId))
            .isInstanceOf(BusinessException.class);
    }

    /**
     * [성공 케이스]
     */
    @Test
    @DisplayName("참여자가 없어도 모임 현황을 조회한다.")
    void success_emptyParticipants() {
        // given
        UUID meetingId = UUID.randomUUID();

        Meeting meeting = mockMeeting(List.of(), 5, LocalDateTime.of(2026, 1, 30, 20, 0));

        given(meetingQueryRepository.findMeetingStatusById(meetingId))
            .willReturn(Optional.of(meeting));

        // when
        GetMeetingStatusResponse response = getMeetingStatusUseCase.execute(meetingId);

        // then
        assertThat(response.getTotalParticipantCount()).isEqualTo(5);
        assertThat(response.getCurrentParticipantCount()).isZero();
        assertThat(response.getPendingParticipantCount()).isEqualTo(5);
        assertThat(response.getParticipants()).isEmpty();

        verify(findStationCoordinateUseCase, times(0))
            .excute(anyString());
    }

    /**
     * [성공 케이스]
     */
    @Test
    @DisplayName("출발지 없는 참가자는 현황에서 제외된다.")
    void success_filterWithoutDeparture() {
        // given
        UUID meetingId = UUID.randomUUID();

        Participant p1 = mockParticipantWithDeparture("이대호", "구로디지털단지역");
        Participant p2 = mockParticipantWithDeparture("최동원", "합정역");
        Participant p3 = mockParticipantWithoutDeparture();

        Meeting meeting = mockMeeting(List.of(p1, p2, p3), 3, LocalDateTime.of(2026, 1, 30, 20, 0));

        given(meetingQueryRepository.findMeetingStatusById(meetingId))
            .willReturn(Optional.of(meeting));

        given(findStationCoordinateUseCase.excute(anyString()))
            .willReturn(StationCoordinate.of(37.0, 127.0));

        // when
        GetMeetingStatusResponse response = getMeetingStatusUseCase.execute(meetingId);

        // then
        assertThat(response.getCurrentParticipantCount()).isEqualTo(2);
        assertThat(response.getPendingParticipantCount()).isEqualTo(1);
        assertThat(response.getParticipants()).hasSize(2);

        verify(findStationCoordinateUseCase, times(2))
            .excute(anyString());
    }


    /**
     * 출발지를 설정한 참가자 mock
     */
    private Participant mockParticipantWithDeparture(String nickname, String departure) {
        Participant participant = mock(Participant.class);
        given(participant.getNickname()).willReturn(nickname);
        given(participant.getDeparture()).willReturn(departure);
        return participant;
    }

    /**
     * 출발지를 설정하지 않은 참가자 mock
     */
    private Participant mockParticipantWithoutDeparture() {
        Participant participant = mock(Participant.class);
        given(participant.getDeparture()).willReturn(null);
        return participant;
    }

    /**
     * 모임 mock
     */
    private Meeting mockMeeting(List<Participant> participants, int count, LocalDateTime deadline) {
        Meeting meeting = mock(Meeting.class);
        given(meeting.getParticipants()).willReturn(participants);
        given(meeting.getCount()).willReturn(count);
        given(meeting.getDeadline()).willReturn(deadline);
        return meeting;
    }
}