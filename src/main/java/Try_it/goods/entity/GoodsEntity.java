package Try_it.goods.entity;

import Try_it.cart.CartEntity;
import Try_it.favorites.FavoritesEntity;
import Try_it.order.OrderListEntity;
import Try_it.order.PayEntity;
import Try_it.review.ReviewEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLDelete(sql = "UPDATE goods SET g_deleted_at = now() WHERE g_idx = ?")
@Table(name = "goods")
public class GoodsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g_idx", updatable = false)
    private Long goodsIdx;

    @Column(name = "g_name", nullable = false, length = 100)
    private String goodsName;

    @Column(name = "g_description", nullable = false)
    private String goodsDescription;

    @Column(name = "g_img_cnt")
    private Integer goodsImgCount;

    @Column(name = "g_price", nullable = false)
    private Integer goodsPrice;

    @Column(name = "g_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp goodsCreatedAt;

    @Column(name = "g_updated_at")
    @UpdateTimestamp
    private Timestamp goodsUpdatedAt;

    @Column(name = "g_deleted_at")
    private Timestamp goodsDeletedAt;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<GoodsCategoriesMappingEntity> categories;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReviewEntity> review;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderListEntity> orderList;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FavoritesEntity> favorites;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PayEntity> pay;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CartEntity> cart;
}
