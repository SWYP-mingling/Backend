package swyp.mingling.domain.meeting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.mingling.global.entity.BaseTimeEntity;

/**
 * 모임-목적 매핑 엔티티
 * Schema: meeting_purpose_mapping
 */
@Entity
@Table(name = "meeting_purpose_mapping")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingPurposeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purpose_id", nullable = false)
    private MeetingPurpose purpose;

    @Builder
    public MeetingPurposeMapping(Meeting meeting, MeetingPurpose purpose) {
        this.meeting = meeting;
        this.purpose = purpose;
    }
}
