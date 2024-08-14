package Try_it.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "회원 정보")
public class UserDTO {
    @Schema(description = "회원 인덱스", example = "1")
    private Long userIdx;

    @Schema(description = "회원 아이디", example = "chuchu")
    private String userId;

    @Schema(description = "회원 이름", example = "추수연")
    private String userName;

    @Schema(description = "회원 비밀번호", example = "1234")
    private String userPassword;

    @Schema(description = "회원 이메일", example = "chu@naver.com")
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
}
