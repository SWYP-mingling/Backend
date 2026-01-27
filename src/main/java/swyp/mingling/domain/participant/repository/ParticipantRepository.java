package swyp.mingling.domain.participant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.mingling.domain.participant.entity.Participant;

/**
 * 참여자 레포지토리
 */
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
}
