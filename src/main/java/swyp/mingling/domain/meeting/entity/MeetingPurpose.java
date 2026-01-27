package swyp.mingling.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모임 목적 엔티티
 * Schema: meeting_purpose
 */
@Entity
@Table(name = "meeting_purpose")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingPurpose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purpose_id", nullable = false)
    private Integer purposeId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder
    public MeetingPurpose(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive != null ? isActive : true;
    }

    /**
     * 목적명 수정
     */
    public void updateName(String name) {
        this.name = name;
    }

    /**
     * 활성화 상태 변경
     */
    public void updateIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
