package Try_it.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx", updatable = false)
    private Long userIdx;

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;

    @Column(name = "user_password", nullable = false, length = 100)
    private String userPassword;

    @Column(name = "user_email", nullable = false, length =100)
    private String userEmail;

    @Column(name = "user_phone", nullable = false, length = 30)
    private Integer userPhone;

    @Column(name = "user_gender", nullable = false)
    private Boolean userGender;

    @Column(name = "user_address", nullable = false, length = 225)
    private String userAddress;

    @Column(name = "user_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp userCreatedAt;

    @Column(name = "user_updated_at")
    @UpdateTimestamp
    private Timestamp userUpdatedAt;

    @Column(name = "user_deleted_at")
    private Timestamp userDeletedAt;

    @Column(name = "user_is_admin", nullable = false)
    private Boolean userIsAdmin;

}
