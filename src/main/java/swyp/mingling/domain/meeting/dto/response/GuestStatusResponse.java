package swyp.mingling.domain.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 모임 참여 현황 조회 응답 DTO
 */
@Getter
@AllArgsConstructor
@Schema(description = "모임 참여 현황 조회 응답")
public class GuestStatusResponse {

    private String meetingName;

    private String category;

    @Schema(description = "전체 참여자 수", example = "10")
    private int totalParticipantCount;

    @Schema(description = "출발지 입력 참여자 수", example = "2")
    private int currentParticipantCount;

    @Schema(description = "모임 참여자 목록")
    private List<ParticipantInfo> participants;

    /**
     * 모임 참여자 정보
     */
    @Getter
    @AllArgsConstructor
    @Schema(description = "모임 참여자 정보")
    public static class ParticipantInfo {

        @Schema(description = "참여자 이름", example = "김밍글")
        private String userName;

        public static ParticipantInfo of(
            String userName
        ) {
            return new ParticipantInfo(userName);
        }

    }

    /**
     * 정적 팩토리 메서드
     */
    public static GuestStatusResponse of(
        String meetingName,
        String category,
        int totalParticipantCount,
        int currentParticipantCount,
        List<ParticipantInfo> participants
    ) {
        return new GuestStatusResponse(
            meetingName,
            category,
            totalParticipantCount,
            currentParticipantCount,
            participants);
    }

}
