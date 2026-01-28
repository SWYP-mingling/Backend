package swyp.mingling.domain.participant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import swyp.mingling.domain.meeting.entity.Meeting;
import swyp.mingling.domain.participant.entity.Participant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class EnterMeetingRequest {

    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Schema(description = "사용자 이름", example = "스위프")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "비밀번호", example = "password123")
    private String password;

    @Builder
    public Participant toEntity(Meeting meeting){

        return Participant.builder()
                .nickname(this.userId)
                .password(this.password)
                .meeting(meeting)
                .build();
    }
}