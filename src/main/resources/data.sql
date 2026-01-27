INSERT INTO mingling_dev.meeting_purpose (name, is_active) VALUES
                                                               ('친목', TRUE),
                                                               ('업무', TRUE),
                                                               ('스터디', TRUE),
                                                               ('운동', TRUE),
                                                               ('취미', TRUE);


INSERT INTO participant (uuid, meeting_id, nickname, password, departure, time, is_deleted) VALUES
(REPLACE(UUID(), '-', ''), (SELECT id FROM meeting ORDER BY created_at DESC LIMIT 1 OFFSET 2), '강남피플', '1234', '양재역', 15, FALSE),
(REPLACE(UUID(), '-', ''), (SELECT id FROM meeting ORDER BY created_at DESC LIMIT 1 OFFSET 1), '홍대짱', '5678', '신촌역', 10, FALSE),
(REPLACE(UUID(), '-', ''), (SELECT id FROM meeting ORDER BY created_at DESC LIMIT 1 OFFSET 0), '산책러', '9012', '송파역', 20, FALSE);

INSERT INTO mingling_dev.hot_place (name, latitude, longitude) VALUES
                                                                   ('강남역', 37.4979502, 127.0276368),
                                                                   ('홍대입구역', 37.5572229, 126.9239067),
                                                                   ('서울역', 37.554648, 126.972559),
                                                                   ('잠실역', 37.5133016, 127.1000575),
                                                                   ('합정역', 37.5484757, 126.912071);

-- 1번째 모임 참여자
INSERT INTO mingling_dev.participant (meeting_id, nickname, password, departure, time) VALUES
                                                                                           ('40148038fb7511f094663a9f417233ce', '심세영', '1234', '양재역', 10),
                                                                                           ('40148038fb7511f094663a9f417233ce', '조민아', '1234', '논현역', 15);

-- 2번째 모임 참여자
INSERT INTO mingling_dev.participant (meeting_id, nickname, password, departure, time) VALUES
                                                                                           ('4014851dfb7511f094663a9f417233ce', '강경훈', '1234', '합정역', 5),
                                                                                           ('4014851dfb7511f094663a9f417233ce', '이용석', '1234', '망원역', 20);

-- 3번째 모임 참여자
INSERT INTO mingling_dev.participant (meeting_id, nickname, password, departure, time) VALUES
                                                                                           ('4014864efb7511f094663a9f417233ce', '임준식', '1234', '송파역', 10),
                                                                                           ('4014864efb7511f094663a9f417233ce', '안가연', '1234', '수원역', 60);
