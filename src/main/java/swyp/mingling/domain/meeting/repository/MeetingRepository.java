package swyp.mingling.domain.meeting.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swyp.mingling.domain.meeting.dto.response.midpoint.DepartureListResponse;
import swyp.mingling.domain.meeting.entity.Meeting;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 모임 레포지토리
 */
public interface MeetingRepository extends JpaRepository<Meeting, UUID> {

     @Query("""
        SELECT new swyp.mingling.domain.meeting.dto.response.midpoint.DepartureListResponse(p.nickname, p.departure)
        FROM Meeting m
        LEFT JOIN m.participants p
        WHERE m.id = :meetingId and p.departure is not null
    """)
     List<DepartureListResponse> findDeparturesAndNicknameByMeetingId(@Param("meetingId") UUID meetingId);

     @Query("SELECT mp.name FROM Meeting m " +
             "JOIN m.purposeMappings mpm " +
             "JOIN mpm.purpose mp " +
             "WHERE m.id = :meetingId")
     Optional<String> findPurposeNamesByMeetingId(@Param("meetingId") UUID meetingId);
}
