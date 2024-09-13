package Try_it.order;

import Try_it.coupon.CouponEntity;
import Try_it.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "주문 정보")
public class OrderDTO {

    private Long orderPk;

    private Integer orderTotal;

    private String orderRequest;

    private String orderRecipient;

    private String orderAddress;

    private Integer orderPhone;

    private Timestamp orderCreatedAt;

    private Boolean orderIsCancelled;

    private Long user;

    private Long coupon;

    private Long goods;

}
