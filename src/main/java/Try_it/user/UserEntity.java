package Try_it.user;

import Try_it.cart.CartEntity;
import Try_it.coupon.AlarmEntity;
import Try_it.coupon.CouponUserMappingEntity;
import Try_it.review.ReviewEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLDelete(sql = "UPDATE user SET u_deleted_at = now() WHERE u_pk = ?")
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_pk", updatable = false)
    private Long userPk;

    @Column(name = "u_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "u_name", nullable = false, length = 20)
    private String userName;

    @Column(name = "u_password", nullable = false, length = 100)
    private String userPassword;

    @Column(name = "u_email", nullable = false, length =100)
    private String userEmail;

    @Column(name = "u_phone", nullable = false, length = 30)
    private Integer userPhone;

    @Column(name = "u_gender", nullable = false)
    private Boolean userGender;

    @Column(name = "u_address", nullable = false)
    private String userAddress;

    @Column(name = "u_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp userCreatedAt;

    @Column(name = "u_updated_at")
    @UpdateTimestamp
    private Timestamp userUpdatedAt;

    @Column(name = "u_deleted_at")
    private Timestamp userDeletedAt;

    @Column(name = "u_is_admin", nullable = false)
    private Boolean userIsAdmin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AlarmEntity> alarm;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CouponUserMappingEntity> coupons;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReviewEntity> review;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CartEntity> cart;
}
