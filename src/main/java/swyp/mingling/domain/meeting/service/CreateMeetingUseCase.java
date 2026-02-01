package swyp.mingling.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.mingling.domain.meeting.dto.request.CreateMeetingRequest;
import swyp.mingling.domain.meeting.dto.response.CreateMeetingResponse;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.meeting.entity.MeetingPurpose;
import swyp.mingling.domain.meeting.entity.MeetingPurposeMapping;
import swyp.mingling.domain.meeting.repository.MeetingPurposeRepository;
import swyp.mingling.domain.meeting.repository.MeetingRepository;
import swyp.mingling.global.exception.BusinessException;
import swyp.mingling.global.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateMeetingUseCase {

    private final MeetingRepository meetingRepository;
    private final MeetingPurposeRepository meetingPurposeRepository;

    /**
     * 모임 생성
     *
     * @param request 모임 생성 요청 DTO
     * @return 생성된 모임 URL
     */
    public CreateMeetingResponse createMeeting(CreateMeetingRequest request) {
        // 1. 목적명 리스트로 MeetingPurpose 엔티티 조회
        List<MeetingPurpose> purposes = meetingPurposeRepository.findByNameIn(request.getPurposes());

        if (purposes.size() != request.getPurposes().size()) {
            // '회의', '친목'은 에러를 발생시키지 않고 pass (방어 로직)
            List<String> validPurposes = request.getPurposes().stream()
                    .filter(p -> !p.equals("회의") && !p.equals("친목"))
                    .collect(Collectors.toList());

            // '회의', '친목'을 제외한 나머지 목적명들 중 정의되지 않은 것이 있으면 예외 발생
            if (purposes.size() != validPurposes.size()) {
                throw new BusinessException(ErrorCode.PURPOSE_NOT_FOUND);
            }
        }

        // 2. Meeting 엔티티 생성
        Meeting meeting = Meeting.builder()
                .name(request.getMeetingName())
                .hotPlace(null)
                .count(request.getCapacity())
                .deadline(request.getDeadline())
                .status("ACTIVE")
                .build();

        // 3. Meeting 먼저 저장 (ID 생성)
        Meeting savedMeeting = meetingRepository.save(meeting);

        // 4. 여러 개의 목적을 매핑 테이블에 저장
        for (MeetingPurpose purpose : purposes) {
            MeetingPurposeMapping mapping = MeetingPurposeMapping.builder()
                    .meeting(savedMeeting)
                    .purpose(purpose)
                    .build();
            savedMeeting.getPurposeMappings().add(mapping);
        }

        // 5. 모임 URL 반환
        String meetingUrl = "https://mingling.kr/meeting/" + savedMeeting.getId();
        String meetingId = savedMeeting.getId().toString();

        return new CreateMeetingResponse(meetingUrl, meetingId);
    }
}
