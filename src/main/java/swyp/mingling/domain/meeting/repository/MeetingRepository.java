package swyp.mingling.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.mingling.domain.meeting.entity.Meeting;

import java.util.UUID;

/**
 * 모임 레포지토리
 */
public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
}
