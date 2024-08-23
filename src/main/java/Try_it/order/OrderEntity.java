package Try_it.order;

import Try_it.coupon.CouponEntity;
import Try_it.review.ReviewEntity;
import Try_it.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "o_idx", updatable = false)
    private Long orderIdx;

    @Column(name = "o_total", nullable = false)
    private Integer orderTotal;

    @Column(name = "o_request")
    private String orderRequest;

    @Column(name = "o_recipient", nullable = false, length = 50)
    private String orderRecipient;

    @Column(name = "o_address", nullable = false)
    private String orderAddress;

    @Column(name = "o_phone", nullable = false)
    private Integer orderPhone;

    @Column(name = "o_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp orderCreatedAt;

    @Column(name = "o_is_cancelled", nullable = false)
    private Boolean orderIsCancelled;

    @ManyToOne
    @JoinColumn(name = "u_idx", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "c_idx", nullable = false)
    @JsonBackReference
    private CouponEntity coupon;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderListEntity> orderList;
}
