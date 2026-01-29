package swyp.mingling.domain.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 모임 참여 현황 조회 응답 DTO
 */
@Getter
@AllArgsConstructor
@Schema(description = "모임 참여 현황 조회 응답")
public class GetMeetingStatusResponse {

    @Schema(description = "전체 참여자 수", example = "10")
    private int totalParticipantCount;

    @Schema(description = "출발지 입력 참여자 수", example = "2")
    private int currentParticipantCount;

    @Schema(description = "출발지 미입력 참여자 수", example = "8")
    private int pendingParticipantCount;

    @Schema(description = "모임 마감 시간", example = "2026-01-23T23:00:00")
    private LocalDateTime deadlineAt;

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

        @Schema(description = "출발역 이름", example = "구로디지털단지역")
        private String stationName;

        @Schema(description = "위도", example = "37.485266")
        private Double latitude;

        @Schema(description = "경도", example = "126.901401")
        private Double longitude;

        public static ParticipantInfo of(
            String userName,
            String stationName,
            Double latitude,
            Double longitude
        ) {
            return new ParticipantInfo(userName, stationName, latitude, longitude);
        }

    }

    /**
     * 정적 팩토리 메서드
     */
    public static GetMeetingStatusResponse of(
        int totalParticipantCount,
        int currentParticipantCount,
        int pendingParticipantCount,
        LocalDateTime deadlineAt,
        List<ParticipantInfo> participants
    ) {
        return new GetMeetingStatusResponse(
            totalParticipantCount,
            currentParticipantCount,
            pendingParticipantCount,
            deadlineAt,
            participants);
    }

}
