package swyp.mingling.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swyp.mingling.domain.meeting.entity.HotPlace;

/**
 * 핫플레이스 레포지토리
 */
public interface HotPlaceRepository extends JpaRepository<HotPlace, Integer> {
    @Query(value = "SELECT * FROM hot_place ORDER BY RAND() LIMIT 1", nativeQuery = true)
    HotPlace findRandomHotPlace();
}
