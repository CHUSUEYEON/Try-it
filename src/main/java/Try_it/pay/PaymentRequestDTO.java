package Try_it.pay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequestDTO {
    private  Long user;
    private  Long order;
    private Integer totalPrice;
    private List<Long> goods;
}
