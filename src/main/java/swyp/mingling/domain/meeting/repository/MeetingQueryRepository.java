package swyp.mingling.domain.meeting.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import swyp.mingling.domain.meeting.entity.Meeting;

@Repository
@RequiredArgsConstructor
public class MeetingQueryRepository {

    @PersistenceContext
    private final EntityManager em;

    public Optional<Meeting> findMeetingStatusById(UUID meetingId) {
        return em.createQuery("""
            select distinct m
            from Meeting m
            left join fetch m.participants
            where m.id = :meetingId
            and m.isDeleted = false
            """, Meeting.class)
            .setParameter("meetingId", meetingId)
            .getResultStream()
            .findFirst();
    }
}
