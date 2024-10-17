package Try_it.cart;

import Try_it.goods.GoodsEntity;
import Try_it.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

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
    @Column(name = "c_pk", updatable = false)
    private Long cartPk;

    @Column(name = "c_amount", nullable = false)
    private Integer cartAmount;

    @Column(name = "c_is_paid", nullable = false)
    private Boolean cartIsPaid;

    @Column(name = "c_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp cartCreatedAt;

//    @ManyToOne
//    @JoinColumn(name = "g_pk", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "g_pk", nullable = false)
//    @JsonBackReference
    private GoodsEntity goods;

    @ManyToOne
    @JoinColumn(name = "u_pk", nullable = false)
    @JsonBackReference
    private UserEntity user;
}
