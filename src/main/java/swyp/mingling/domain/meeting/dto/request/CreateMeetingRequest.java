package swyp.mingling.domain.meeting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 모임 생성 요청 DTO
 */
@Getter
public class CreateMeetingRequest {

    @NotBlank(message = "모임명은 필수입니다.")
    @Schema(
            description = "모임명",
            example = "신년회",
            requiredMode = RequiredMode.REQUIRED
    )
    private String meetingName;

    @NotEmpty(message = "모임 목적 리스트는 필수입니다.")
    @Schema(
            description = "모임 목적 리스트",
            example = "[\"친목\", \"회식\", \"스터디\"]",
            requiredMode = RequiredMode.REQUIRED
    )
    private List<String> purposes;

    @NotNull(message = "모임 목적 개수는 필수입니다.")
    @Schema(
            description = "모임 목적 리스트의 총 개수",
            example = "3",
            requiredMode = RequiredMode.REQUIRED
    )
    private Integer purposeCount;

    @NotNull(message = "인원은 필수입니다.")
    @Schema(
            description = "모임 인원",
            example = "10",
            requiredMode = RequiredMode.REQUIRED
    )
    private Integer capacity;

    @NotNull(message = "마감 시간은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(
            description = "모임 마감 시간",
            example = "2026-01-30T23:59:59",
            requiredMode = RequiredMode.REQUIRED,
            type = "string",
            format = "date-time"
    )
    private LocalDateTime deadline;
}
