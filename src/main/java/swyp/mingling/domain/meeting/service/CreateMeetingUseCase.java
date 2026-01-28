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
import swyp.mingling.global.exception.ValidationErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
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
        // 1. 우선순위 순서대로 검증 (첫 번째 에러만 반환)
        Map<String, String> errors = new HashMap<>();

        // 우선순위 1: 마감 기한 검증 (현재 시간 이후여야 함)
        if (request.getDeadline().isBefore(java.time.LocalDateTime.now())) {
            errors.put("deadline", ErrorCode.INVALID_DEADLINE.getMessage());
            throw new ValidationErrorException(errors);
        }

        // 우선순위 2: 정원 검증 (최소 2명 이상)
        if (request.getCapacity() < 2) {
            errors.put("capacity", ErrorCode.INVALID_CAPACITY.getMessage());
            throw new ValidationErrorException(errors);
        }

        // 우선순위 3: 목적명 리스트로 MeetingPurpose 엔티티 조회
        List<MeetingPurpose> purposes = meetingPurposeRepository.findByNameIn(request.getPurposes());

        if (purposes.size() != request.getPurposes().size()) {
            errors.put("purposes", ErrorCode.PURPOSE_NOT_FOUND.getMessage());
            throw new ValidationErrorException(errors);
        }

        // 4. Meeting 엔티티 생성
        Meeting meeting = Meeting.builder()
                .name(request.getMeetingName())
                .hotPlace(null)
                .count(request.getCapacity())
                .deadline(request.getDeadline())
                .status("ACTIVE")
                .build();

        // 5. Meeting 먼저 저장 (ID 생성)
        Meeting savedMeeting = meetingRepository.save(meeting);

        // 6. 여러 개의 목적을 매핑 테이블에 저장
        for (MeetingPurpose purpose : purposes) {
            MeetingPurposeMapping mapping = MeetingPurposeMapping.builder()
                    .meeting(savedMeeting)
                    .purpose(purpose)
                    .build();
            savedMeeting.getPurposeMappings().add(mapping);
        }

        // 7. 모임 URL 반환
        String meetingUrl = "https://mingling.com/meeting/" + savedMeeting.getId();
        String meetingId = savedMeeting.getId().toString();

        return new CreateMeetingResponse(meetingUrl, meetingId);
    }
}
