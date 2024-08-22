package Try_it.coupon;

import Try_it.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "cou_user_mapping")
public class CouponUserMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cou_user_idx", updatable = false)
    private Long couUserIdx;

    @ManyToOne
    @JoinColumn(name = "cou_idx", nullable = false)
    @JsonBackReference
    private CouponEntity coupon;

    @ManyToOne
    @JoinColumn(name = "u_idx", nullable = false)
    @JsonBackReference
    private UserEntity user;
}
