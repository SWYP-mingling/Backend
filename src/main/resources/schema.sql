-- ============================================
-- 엔티티 기준 스키마 (2026-01-28)
-- ============================================

-- 1. 모임 목적 테이블 (BaseTimeEntity 상속 안 함)
CREATE TABLE IF NOT EXISTS mingling_dev.meeting_purpose (
    purpose_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '모임 목적 ID',
    name VARCHAR(30) NOT NULL UNIQUE COMMENT '모임 목적명 (중복 방지)',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '활성화 여부',
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='모임 목적';

-- 2. 핫플레이스 테이블 (BaseTimeEntity 상속 안 함)
CREATE TABLE IF NOT EXISTS mingling_dev.hot_place (
    hot_place_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '핫플레이스 ID',
    name VARCHAR(50) NOT NULL COMMENT '장소명',
    latitude DECIMAL(10, 7) NOT NULL COMMENT '위도',
    longitude DECIMAL(10, 7) NOT NULL COMMENT '경도',
    line VARCHAR(20) COMMENT '지하철 호선'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='핫플레이스';

-- 3. 모임 테이블 (BaseTimeEntity 상속)
CREATE TABLE IF NOT EXISTS mingling_dev.meeting (
    id VARCHAR(36) PRIMARY KEY COMMENT '모임 UUID',
    hot_place_id INT COMMENT '핫플레이스 ID (nullable)',
    name VARCHAR(100) NOT NULL COMMENT '모임명',
    count INT NOT NULL COMMENT '모임 인원',
    deadline DATETIME NOT NULL COMMENT '마감 시간',
    status VARCHAR(20) NOT NULL COMMENT '모임 상태',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
    CONSTRAINT fk_meeting_hot_place FOREIGN KEY (hot_place_id) REFERENCES hot_place(hot_place_id),
    INDEX idx_hot_place_id (hot_place_id),
    INDEX idx_status (status),
    INDEX idx_is_deleted (is_deleted),
    INDEX idx_deadline (deadline)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='모임';

-- 4. 모임-목적 매핑 테이블 (BaseTimeEntity 상속, 다대다 관계)
CREATE TABLE IF NOT EXISTS mingling_dev.meeting_purpose_mapping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '매핑 ID',
    meeting_id VARCHAR(36) NOT NULL COMMENT '모임 UUID',
    purpose_id INT NOT NULL COMMENT '모임 목적 ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
    CONSTRAINT fk_mapping_meeting FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE,
    CONSTRAINT fk_mapping_purpose FOREIGN KEY (purpose_id) REFERENCES meeting_purpose(purpose_id) ON DELETE CASCADE,
    UNIQUE KEY uk_meeting_purpose (meeting_id, purpose_id) COMMENT '중복 방지',
    INDEX idx_meeting_id (meeting_id),
    INDEX idx_purpose_id (purpose_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='모임-목적 매핑';

-- 5. 참여자 테이블 (BaseTimeEntity 상속)
CREATE TABLE IF NOT EXISTS mingling_dev.participant (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '참여자 ID',
    meeting_id VARCHAR(36) NOT NULL COMMENT '모임 UUID',
    nickname VARCHAR(30) NOT NULL COMMENT '닉네임',
    password VARCHAR(255) NOT NULL COMMENT '비밀번호',
    departure VARCHAR(100) COMMENT '출발지 (nullable)',
    time INT COMMENT '이동 시간(분) (nullable)',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
    CONSTRAINT fk_participant_meeting FOREIGN KEY (meeting_id) REFERENCES meeting(id),
    INDEX idx_meeting_id (meeting_id),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='참여자';
;

