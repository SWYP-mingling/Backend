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

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Builder
    public HotPlace(String name, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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
    public void updateLocation(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
