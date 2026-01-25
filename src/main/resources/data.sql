-- 모임 목적 초기 데이터
INSERT INTO meeting_purpose (purpose_id, name, is_active) VALUES
(UNHEX(REPLACE(UUID(), '-', '')), '친목', TRUE),
(UNHEX(REPLACE(UUID(), '-', '')), '업무', TRUE),
(UNHEX(REPLACE(UUID(), '-', '')), '스터디', TRUE),
(UNHEX(REPLACE(UUID(), '-', '')), '운동', TRUE),
(UNHEX(REPLACE(UUID(), '-', '')), '취미', TRUE);

-- 핫플레이스 초기 데이터
INSERT INTO hot_place (hot_place_id, name, latitude, longitude) VALUES
(UNHEX(REPLACE(UUID(), '-', '')), '강남역', 37.4979502, 127.0276368),
(UNHEX(REPLACE(UUID(), '-', '')), '홍대입구역', 37.5572229, 126.9239067),
(UNHEX(REPLACE(UUID(), '-', '')), '서울역', 37.554648, 126.972559),
(UNHEX(REPLACE(UUID(), '-', '')), '잠실역', 37.5133016, 127.1000575),
(UNHEX(REPLACE(UUID(), '-', '')), '합정역', 37.5484757, 126.912071);
