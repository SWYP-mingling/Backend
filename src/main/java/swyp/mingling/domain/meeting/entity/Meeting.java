package swyp.mingling.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import swyp.mingling.domain.participant.entity.Participant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meeting")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purpose_id", nullable = false)
    private MeetingPurpose purpose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hot_place_id", nullable = false)
    private HotPlace hotPlace;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "invite_url", nullable = false, length = 255)
    private String inviteUrl;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public Meeting(MeetingPurpose purpose, HotPlace hotPlace, String name, Integer count,
                   LocalDateTime deadline, String inviteUrl, String status) {
        this.purpose = purpose;
        this.hotPlace = hotPlace;
        this.name = name;
        this.count = count;
        this.deadline = deadline;
        this.inviteUrl = inviteUrl;
        this.status = status;
        this.isDeleted = false;
    }

    /**
     * 모임명 수정
     */
    public void updateName(String name) {
        this.name = name;
    }

    /**
     * 모임 목적 수정
     */
    public void updatePurpose(MeetingPurpose purpose) {
        this.purpose = purpose;
    }

    /**
     * 핫플레이스 수정
     */
    public void updateHotPlace(HotPlace hotPlace) {
        this.hotPlace = hotPlace;
    }

    /**
     * 모임 인원 수정
     */
    public void updateCount(Integer count) {
        this.count = count;
    }

    /**
     * 마감 시간 수정
     */
    public void updateDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /**
     * 초대 URL 수정
     */
    public void updateInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }

    /**
     * 상태 수정
     */
    public void updateStatus(String status) {
        this.status = status;
    }
}
