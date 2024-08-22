package Try_it.goods.entity;

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
@Table(name = "goods_cate_mapping")
public class GoodsCategoriesMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g_cate_idx", updatable = false)
    private Long goodsCateIdx;

    @ManyToOne
    @JoinColumn(name = "cate_idx", nullable = false)
    @JsonBackReference
    private CategoriesEntity category;

    @ManyToOne
    @JoinColumn(name = "g_idx", nullable = false)
    @JsonBackReference
    private GoodsEntity goods;
}
