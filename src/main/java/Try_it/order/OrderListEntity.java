package Try_it.order;

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
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "order_list")
public class OrderListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ol_idx", updatable = false)
    private Long orderListIdx;

    @ManyToOne
    @JoinColumn(name = "g_idx", nullable = false)
    @JsonBackReference
    private GoodsEntity goods;

    @ManyToOne
    @JoinColumn(name = "o_idx", nullable = false)
    @JsonBackReference
    private OrderEntity order;

}

