package Try_it.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDTO {
    @Schema(description = "회원 아이디", example = "admin123")
//    @Pattern(regexp = "(?![0-9])(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z][a-zA-Z0-9].{3,19}", message = "아이디는 숫자로 시작하지 않는 길이 4~20자, 영어 대소문자, 숫자로 이루어진 문자열이어야 합니다.")
    private String userId;

    @Schema(description = "회원 비밀번호", example = "admin123!")
//    @Pattern(regexp = "(?![0-9])(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}", message = "비밀번호는 영어 대소문자, 숫자, 특수문자가 적어도 한 개 이상으로 이루어진 8자 이상의 문자열이어야 합니다.")
    private String userPassword;

    @Schema(description = "JWT Token", example = "Response 로 토큰 값 확인하기 위해 만든 필드입니다. 로그인 실행 시, 지워주세요.", nullable = true)
    private String token;
}
