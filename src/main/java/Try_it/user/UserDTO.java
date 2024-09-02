package Try_it.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "회원 정보")
public class UserDTO {
    @Schema(description = "회원 인덱스", example = "1")
    private Long userPk;

    @Schema(description = "회원 아이디", example = "chuchu")
//    @Pattern(regexp = "(?![0-9])(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z][a-zA-Z0-9].{3,19}", message = "아이디는 숫자로 시작하지 않는 길이 4~20자, 영어 대소문자, 숫자로 이루어진 문자열이어야 합니다.")
    private String userId;

    @Schema(description = "회원 이름", example = "추수연")
    private String userName;

    @Schema(description = "회원 비밀번호", example = "qwer1234!")
//    @Pattern(regexp = "(?![0-9])(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}", message = "비밀번호는 영어 대소문자, 숫자, 특수문자가 적어도 한 개 이상으로 이루어진 8자 이상의 문자열이어야 합니다.")
    private String userPassword;

    @Schema(description = "회원 이메일", example = "chu@naver.com")
//    @Pattern(regexp = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
    private String userEmail;

    @Schema(description = "회원 전화번호", example = "01012345678")
    private Integer userPhone;

    @Schema(description = "회원 성별, 남자=false, 여자=true", example = "true")
    private Boolean userGender;

    @Schema(description = "회원 주소", example = "서울시 서대문구 홍은동")
    private String userAddress;

    @Schema(description = "회원 생성일자", example = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp userCreatedAt;

    @Schema(description = "회원 수정일자", example = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", nullable = true)
    private Timestamp userUpdatedAt;

    @Schema(description = "회원 삭제일자, 탈퇴한 회원이면 타임스탬프, 아니면 Null", example = "null", nullable = true)
    private Timestamp userDeletedAt;

    @Schema(description = "회원 = 0, 관리자 =1 (default value = 0)", example = "false")
    private Boolean userIsAdmin;

    @Schema(description = "JWT Token", example = "eyJhbGciO.eyJhbGci", nullable = true)
    private String token;

    private List alarms;
    private List carts;
    private List coupons;
    private List reviews;
}
