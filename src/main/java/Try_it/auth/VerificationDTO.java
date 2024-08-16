package Try_it.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Schema(description = "핸드폰/메일 인증")
public class VerificationDTO {
    @Schema(description = "이메일", example = "test@example.com")
    @Pattern(regexp = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
    private String email;
    @Schema(description = "핸드폰 번호", example = "01012345678")
    @Pattern(regexp = "^010\\d{8}$", message = "01012345678 형식에 맞게 입력해주세요.")
    private String phone;
    private String code;

}
