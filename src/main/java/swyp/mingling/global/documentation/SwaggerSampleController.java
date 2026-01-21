package swyp.mingling.global.documentation;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swyp.mingling.global.response.ApiResponse;

/**
 * Swagger 사용법을 안내하기 위한 샘플 컨트롤러
 */
@Tag(name = "Swagger 샘플 API", description = "Swagger 사용법 예제 모음")
@RestController
@RequestMapping("/sample")
public class SwaggerSampleController {

    /**
     * 샘플 GET API
     * RequestParam 사용 예제 및 Swagger 문서화 적용 예시.
     *
     * @param userId 조회할 사용자 ID
     * @return 사용자 정보가 포함된 응답
     */
    @SampleApiDocumentation.GetSampleDoc(userId = 1)
    @GetMapping("/get")
    public ApiResponse<SampleResponse> getSample(@RequestParam Long userId) {
        return ApiResponse.success(
            new SampleResponse(userId, "테스트 사용자")
        );
    }

    /**
     * 샘플 POST API
     * RequestBody + DTO 사용 예제와 Swagger 문서화 적용 예시.
     *
     * @param request 사용자 요청 DTO
     * @return 생성된 사용자 정보 응답
     */
    @SampleApiDocumentation.PostSampleDoc
    @PostMapping("/post")
    public ApiResponse<SampleResponse> postSample(@RequestBody SampleRequest request) {
        return ApiResponse.success(
            new SampleResponse(request.getUserId(), request.getName())
        );
    }

    /**
     * Sample POST 요청 DTO
     */
    @Getter
    public static class SampleRequest {

        @NotBlank(message = "ID 는 필수입니다.")
        @Schema(
            description = "사용자 ID",
            example = "1",
            requiredMode = RequiredMode.REQUIRED
        )
        private Long userId;

        @Schema(
            description = "사용자 이름",
            example = "홍길동",
            requiredMode = RequiredMode.NOT_REQUIRED
        )
        private String name;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime createdAt;
    }

    /**
     * Sample API 응답 DTO
     */
    @Getter
    @AllArgsConstructor
    public static class SampleResponse {

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;
    }
}
