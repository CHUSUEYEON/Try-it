package Try_it.goods.entity;

import Try_it.category.CategoriesEntity;
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
    @Column(name = "g_cate_pk", updatable = false)
    private Long goodsCatePk;

    @ManyToOne
    @JoinColumn(name = "cate_pk", nullable = false)
    @JsonBackReference
    private CategoriesEntity category;

    @ManyToOne
    @JoinColumn(name = "g_pk", nullable = false)
    @JsonBackReference
    private GoodsEntity goods;
}
