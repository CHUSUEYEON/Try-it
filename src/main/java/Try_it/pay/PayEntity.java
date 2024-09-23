package Try_it.pay;

import Try_it.goods.GoodsEntity;
import Try_it.order.OrderEntity;
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
@Table(name = "pay")
public class PayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_pk", updatable = false)
    private Long payPk;

    @Column(name = "p_code", nullable = false, length = 30)
    private String payCode;

    @Column(name = "p_is_refunded", nullable = false)
    private Boolean payIsRefunded;

    @ManyToOne
    @JoinColumn(name = "g_pk", nullable = false)
    @JsonBackReference
    private GoodsEntity goods;

    @ManyToOne
    @JoinColumn(name = "o_pk", nullable = false)
    @JsonBackReference
    private OrderEntity order;
}
