package swyp.mingling.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.mingling.domain.meeting.entity.MeetingPurpose;

import java.util.List;
import java.util.Optional;

/**
 * 모임 목적 레포지토리
 */
public interface MeetingPurposeRepository extends JpaRepository<MeetingPurpose, Integer> {
    Optional<MeetingPurpose> findByName(String name);
    List<MeetingPurpose> findByNameIn(List<String> names);
}
