package swyp.mingling.domain.participant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.participant.entity.Participant;

import java.util.Optional;
import java.util.UUID;

/**
 * 참여자 레포지토리
 */
public interface ParticipantRepository extends JpaRepository<Participant, UUID> {


    boolean existsByMeetingAndNicknameAndPassword(Meeting meeting,
                                                  String nickname,
                                                  String password);

    boolean existsByMeetingAndNickname(Meeting meeting,
                                      String nickname);

}
