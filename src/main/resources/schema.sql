-- 기존 테이블 삭제 (역순으로 삭제)
DROP TABLE IF EXISTS participant;
DROP TABLE IF EXISTS meeting;
DROP TABLE IF EXISTS hot_place;
DROP TABLE IF EXISTS meeting_purpose;

-- 모임 목적 테이블
CREATE TABLE meeting_purpose (
    purpose_id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 핫플레이스 테이블
CREATE TABLE hot_place (
    hot_place_id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 모임 테이블
CREATE TABLE meeting (
    id BINARY(16) NOT NULL PRIMARY KEY,
    purpose_id BINARY(16) NOT NULL,
    hot_place_id BINARY(16) NOT NULL,
    name VARCHAR(100) NOT NULL,
    count INT NOT NULL,
    deadline DATETIME NOT NULL,
    invite_url VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (purpose_id) REFERENCES meeting_purpose(purpose_id),
    FOREIGN KEY (hot_place_id) REFERENCES hot_place(hot_place_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 참여자 테이블
CREATE TABLE participant (
    uuid BINARY(16) NOT NULL PRIMARY KEY,
    meeting_id BINARY(16) NOT NULL,
    nickname VARCHAR(30) NOT NULL,
    password VARCHAR(255) NOT NULL,
    departure VARCHAR(100) NOT NULL,
    time INT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (meeting_id) REFERENCES meeting(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
