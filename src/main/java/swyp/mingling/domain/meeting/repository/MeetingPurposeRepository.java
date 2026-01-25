package swyp.mingling.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.mingling.domain.meeting.entity.MeetingPurpose;

import java.util.UUID;

/**
 * 모임 목적 레포지토리
 */
public interface MeetingPurposeRepository extends JpaRepository<MeetingPurpose, UUID> {
}
