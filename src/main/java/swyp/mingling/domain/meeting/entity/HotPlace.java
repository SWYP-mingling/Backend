package swyp.mingling.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 핫플레이스 엔티티
 * Schema: hot_place
 */
@Entity
@Table(name = "hot_place")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HotPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hot_place_id", nullable = false)
    private Integer hotPlaceId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "line", length = 20)
    private String line;

    @Builder
    public HotPlace(String name, double latitude, double longitude, String line) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.line = line;
    }

    /**
     * 장소명 수정
     */
    public void updateName(String name) {
        this.name = name;
    }

    /**
     * 위치 정보 수정
     */
    public void updateLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
