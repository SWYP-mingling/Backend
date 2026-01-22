package swyp.mingling.domain.meeting.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendResponse {

    @Schema(description = "추천 장소명", example = "맛있는 이자카야")
    private String title;

    @Schema(description = "도로명 주소", example = "서울특별시 마포구 양화로 123")
    private String roadAddress;
}
