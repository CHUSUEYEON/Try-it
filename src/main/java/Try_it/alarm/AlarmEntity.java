package Try_it.alarm;

import Try_it.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import kotlin.time.TimeSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "alarm")
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_pk", updatable = false)
    private Long alarmPk;

//    @Column(name = "a_id", nullable = false)
//    private String alarmId;
    // 왜 필요한지 찾아오기

    @Column(name = "a_content", nullable = false)
    private String alarmContent;

    @Column(name = "a_title", nullable = false, length = 100)
    private String alarmTitle;

    @Column(name = "a_is_read", nullable = false)
    @ColumnDefault("false")
    private Boolean alarmIsRead;

//    @Column(name = "a_receiver", nullable = false)
//    private String alarmReceiver;

    @Column(name = "a_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp alarmCreatedAt;

    // 알림을 클릭했을 때 사용자가 이동할 페이지나 리소스를 지정
    @Column(name = "a_url")
    private String alarmUrl;

    @ManyToOne
    @JoinColumn(name = "u_pk", nullable = false)
    @JsonBackReference
    private UserEntity user;
}
