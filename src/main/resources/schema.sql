-- 1. 모임 목적 테이블
CREATE TABLE mingling_dev.meeting_purpose (
                                              purpose_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '모임 목적 ID',
                                              name VARCHAR(30) NOT NULL COMMENT '모임 목적명',
                                              is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '활성화 여부'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='모임 목적';

-- 2. 핫플레이스 테이블
CREATE TABLE mingling_dev.hot_place (
                                        hot_place_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '핫플레이스 ID',
                                        name VARCHAR(50) NOT NULL COMMENT '장소명',
                                        latitude DECIMAL(10, 7) NOT NULL COMMENT '위도',
                                        longitude DECIMAL(10, 7) NOT NULL COMMENT '경도'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='핫플레이스';

-- 3. 모임 테이블
CREATE TABLE mingling_dev.meeting (
                                      id VARCHAR(36) PRIMARY KEY COMMENT '모임 UUID',
                                      purpose_id INT NOT NULL COMMENT '모임 목적 ID',
                                      hot_place_id INT NULL COMMENT '핫플레이스 ID',
                                      name VARCHAR(100) NOT NULL COMMENT '모임명',
                                      count INT NOT NULL COMMENT '모임 인원',
                                      deadline DATETIME NOT NULL COMMENT '마감 시간',
                                      status VARCHAR(20) NOT NULL COMMENT '모임 상태',
                                      is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
                                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
                                      updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
                                      CONSTRAINT fk_meeting_purpose FOREIGN KEY (purpose_id) REFERENCES meeting_purpose(purpose_id),
                                      CONSTRAINT fk_meeting_hot_place FOREIGN KEY (hot_place_id) REFERENCES hot_place(hot_place_id),
                                      INDEX idx_purpose_id (purpose_id),
                                      INDEX idx_hot_place_id (hot_place_id),
                                      INDEX idx_status (status),
                                      INDEX idx_is_deleted (is_deleted),
                                      INDEX idx_deadline (deadline)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='모임';

-- 4. 참여자 테이블
CREATE TABLE mingling_dev.participant (
                                          id INT AUTO_INCREMENT PRIMARY KEY COMMENT '참여자 ID',
                                          meeting_id VARCHAR(36) NOT NULL COMMENT '모임 ID',
                                          nickname VARCHAR(30) NOT NULL COMMENT '닉네임',
                                          password VARCHAR(255) NOT NULL COMMENT '비밀번호',
                                          departure VARCHAR(100) COMMENT '출발지',
                                          time INT COMMENT '이동 시간(분)',
                                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
                                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
                                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
                                          CONSTRAINT fk_participant_meeting FOREIGN KEY (meeting_id) REFERENCES meeting(id),
                                          INDEX idx_meeting_id (meeting_id),
                                          INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='참여자';

-- 5. 모임-목적 매핑 테이블
CREATE TABLE mingling_dev.meeting_purpose_mapping (
                                                      mapping_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '매핑 ID',
                                                      meeting_id VARCHAR(36) NOT NULL COMMENT '모임 UUID',
                                                      purpose_id INT NOT NULL COMMENT '모임 목적 ID',
                                                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '매핑 생성 시간',

-- 외래키 설정
                                                      CONSTRAINT fk_mapping_meeting FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE,
                                                      CONSTRAINT fk_mapping_purpose FOREIGN KEY (purpose_id) REFERENCES meeting_purpose(purpose_id) ON DELETE CASCADE,

-- 성능을 위한 인덱스 및 중복 방지 유니크 키
                                                      UNIQUE KEY uq_meeting_purpose (meeting_id, purpose_id),
                                                      INDEX idx_meeting_id (meeting_id),
                                                      INDEX idx_purpose_id (purpose_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='모임-목적 매핑';

-- ON DELETE CASCADE: 모임이 삭제되거나 목적 자체가 삭제될 때, 매핑 데이터도 자동으로 지워지도록 설정. (데이터 무결성 유지)
-- UNIQUE KEY: 동일한 모임에 똑같은 목적이 두 번 들어가지 않도록 (meeting_id, purpose_id) 조합을 유니크로 설정.