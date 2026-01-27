package swyp.mingling.domain.participant.entity;

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
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.global.entity.BaseTimeEntity;

/**
 * 참여자 엔티티
 * Schema: participant
 */
@Entity
@Table(name = "participant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "departure", length = 100)
    private String departure;

    @Column(name = "time")
    private Integer time;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public Participant(Meeting meeting, String nickname, String password, String departure, Integer time) {
        this.meeting = meeting;
        this.nickname = nickname;
        this.password = password;
        this.departure = departure;
        this.time = time;
        this.isDeleted = false;
    }

    /**
     * 모임 설정 (양방향 관계 편의 메서드에서 사용)
     */
    protected void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    /**
     * 닉네임 수정
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 비밀번호 수정
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    /**
     * 출발지 정보 수정
     */
    public void updateDeparture(String departure) {
        this.departure = departure;
    }

    /**
     * 이동 시간 수정
     */
    public void updateTime(Integer time) {
        this.time = time;
    }

    /**
     * 소프트 삭제
     */
    public void delete() {
        this.isDeleted = true;
    }

    /**
     * 삭제 취소
     */
    public void restore() {
        this.isDeleted = false;
    }
}
