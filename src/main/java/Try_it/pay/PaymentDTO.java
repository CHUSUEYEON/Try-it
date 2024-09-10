package Try_it.pay;

import Try_it.goods.GoodsEntity;
import Try_it.order.OrderEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "결제 정보")
public class PaymentDTO {
    private Long payPk;
    private String payCode;
    private Boolean payIsRefunded;
    private GoodsEntity goods;
    private OrderEntity order;
}
