package Try_it.coupon;

import Try_it.order.OrderEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "coupon")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cou_idx", updatable = false)
    private Long couponIdx;
    //Todo: idx -> pk

    @Column(name = "cou_name", nullable = false, length = 100)
    private String couponName;

    @Column(name = "cou_discount")
    private Integer couponDiscountRate;

    @Column(name = "cou_price")
    private Integer couponPrice;

    @Column(name = "cou_code", nullable = false, length =50)
    private String couponCode;

    @Column(name = "cou_deadline", nullable = false)
    private String couponDeadline;

    @Column(name = "cou_content", nullable = false)
    private String couponContent;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderEntity> order;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CouponUserMappingEntity> user;
}
