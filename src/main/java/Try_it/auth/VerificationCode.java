package Try_it.auth;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VerificationCode {
    private String code;
    private LocalDateTime createAt;
    private Integer expirationTimeInMinutes;

    public Boolean isExpired(LocalDateTime verifiedAt){
        LocalDateTime expiredAt = createAt.plusMinutes(expirationTimeInMinutes);
        return verifiedAt.isAfter(expiredAt);
    }

    public String generateCodeMessage(String type){
        String message = "";

        if(type == "sms") {
           message = String.format("[Try-it] 인증 코드는 %s 입니다", code);
        } else {
            String formattedExpiredAt = createAt
                .plusMinutes(expirationTimeInMinutes)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            message = String.format(
                """
                    [Verification Code]
                    %s
                    Expired at : %s
                    """,
                code,
                formattedExpiredAt
            );
        }
        return message;
    }
}
//인증 코드를 나타내는 클래스. 식별을 위한 고유한 ID와 인증 코드, 만료 시간(분)을 필드로 가진다.
