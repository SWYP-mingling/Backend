DELETE FROM `hot_place`;
INSERT INTO `hot_place` (`hot_place_id`, `name`, `latitude`, `longitude`) VALUES
                                                                              (1, '강남역', 37.4979502, 127.0276368),
                                                                              (2, '홍대입구역', 37.5572229, 126.9239067),
                                                                              (3, '서울역', 37.5546480, 126.9725590),
                                                                              (4, '잠실역', 37.5133016, 127.1000575),
                                                                              (5, '합정역', 37.5484757, 126.9120710);

DELETE FROM `meeting`;
INSERT INTO `meeting` (`id`, `name`, `count`, `deadline`, `status`, `is_deleted`, `created_at`, `updated_at`) VALUES
                    ('1c14d9ce-186b-41b3-971c-1f0d3dacecbc', '스위프 스터디2', 5, '2026-02-10 18:00:00', 'ACTIVE', 0, '2026-01-28 01:23:44', '2026-01-28 01:23:44'),
                    ('556453cb-a84e-486d-8079-9fa11585ab58', '스위프 스터디2', 5, '2026-02-10 18:00:00', 'ACTIVE', 0, '2026-01-28 01:19:47', '2026-01-28 01:19:47'),
                    ('eca30e6d-6d00-4c9d-8fb4-875d6ac5c80d', '스위프 스터디2', 5, '2026-02-10 18:00:00', 'ACTIVE', 0, '2026-01-28 01:19:36', '2026-01-28 01:19:36');

DELETE FROM `meeting_purpose`;
INSERT INTO `meeting_purpose` (`purpose_id`, `name`, `is_active`) VALUES
                                                                      (1, '맛집', 1),
                                                                      (2, '술집', 1),
                                                                      (3, '카페', 1),
                                                                      (4, '놀거리', 1),
                                                                      (5, '스터디카페', 1),
                                                                      (7, '장소대여', 1);



DELETE FROM `meeting_purpose_mapping`;
INSERT INTO `meeting_purpose_mapping` (`id`, `meeting_id`, `purpose_id`, `created_at`, `updated_at`) VALUES
                                                                                                   (1, 'eca30e6d-6d00-4c9d-8fb4-875d6ac5c80d', 1, '2026-01-27 16:19:36', '2026-01-27 16:19:36'),
                                                                                                   (2, 'eca30e6d-6d00-4c9d-8fb4-875d6ac5c80d', 2, '2026-01-27 16:19:36', '2026-01-27 16:19:36'),
                                                                                                   (3, 'eca30e6d-6d00-4c9d-8fb4-875d6ac5c80d', 3, '2026-01-27 16:19:36', '2026-01-27 16:19:36'),
                                                                                                   (4, '556453cb-a84e-486d-8079-9fa11585ab58', 1, '2026-01-27 16:19:47', '2026-01-27 16:19:47'),
                                                                                                   (5, '556453cb-a84e-486d-8079-9fa11585ab58', 2, '2026-01-27 16:19:47', '2026-01-27 16:19:37'),
                                                                                                   (6, '1c14d9ce-186b-41b3-971c-1f0d3dacecbc', 1, '2026-01-27 16:23:44', '2026-01-27 16:23:44'),
                                                                                                   (7, '1c14d9ce-186b-41b3-971c-1f0d3dacecbc', 2, '2026-01-27 16:23:44', '2026-01-27 16:23:44');

DELETE FROM `participant`;
INSERT INTO `participant` (`id`, `meeting_id`, `nickname`, `password`, `departure`, `time`, `is_deleted`, `created_at`, `updated_at`) VALUES
                        (2, '556453cb-a84e-486d-8079-9fa11585ab58', '강남피플', '1234', '양재역', 15, 0, '2026-01-28 01:33:34', '2026-01-28 01:33:34'),
                        (3, '1c14d9ce-186b-41b3-971c-1f0d3dacecbc', '홍대짱', '5678', '신촌역', 10, 0, '2026-01-28 01:33:34', '2026-01-28 01:33:34'),
                        (4, 'eca30e6d-6d00-4c9d-8fb4-875d6ac5c80d', '산책러', '9012', '송파역', 20, 0, '2026-01-28 01:33:34', '2026-01-28 01:37:09');

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
