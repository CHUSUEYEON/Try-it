package Try_it.alarm;

import Try_it.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "알림함 정보")
public class AlarmDTO {

    @Schema(description = "알림 인덱스", example = "1")
    private Long alarmPk;

//    @Schema(description = "알림 고유 id")
//    private String alarmId;    @Schema(description = "알림 고유 id")
//    private String alarmId;

    @Schema(description = "알림 내용", example = "알림입니다.")
    private String alarmContent;

    @Schema(description = "알림 제목", example = "알림 제목")
    private String alarmTitle;

    @Schema(description = "알림 읽음 여부", example = "true")
    private Boolean alarmIsRead;

    @Schema(description = "알림 수신자", example = "사용자")
    private String alarmReceiver;

    @Schema(description = "알림 생성일", example = "2024-12-31")
    private Timestamp alarmCreatedAt;

    // 알림을 클릭했을 때 사용자가 이동할 페이지나 리소스를 지정
//    @Schema(description = "알림 경로", example = "sdfsdfsd.com")
//    private String alarmUrl;

    @Schema(description = "알림 - 회원 리스트")
    private List user;
}
