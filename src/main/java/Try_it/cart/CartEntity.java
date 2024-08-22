package Try_it.cart;

import Try_it.goods.entity.GoodsEntity;
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
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_idx", updatable = false)
    private Long cartIdx;

    @Column(name = "c_amount", nullable = false)
    private Integer cartAmount;

    @Column(name = "c_is_paid", nullable = false)
    private Boolean cartIsPaid;

    @ManyToOne
    @JoinColumn(name = "g_idx", nullable = false)
    @JsonBackReference
    private GoodsEntity goods;

    @ManyToOne
    @JoinColumn(name = "u_idx", nullable = false)
    @JsonBackReference
    private UserEntity user;
}
