package swyp.mingling.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.mingling.domain.meeting.entity.HotPlace;

import java.util.UUID;

/**
 * 핫플레이스 레포지토리
 */
public interface HotPlaceRepository extends JpaRepository<HotPlace, UUID> {
}
