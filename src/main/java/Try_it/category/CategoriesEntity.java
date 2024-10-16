package Try_it.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "category")
public class CategoriesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "cate_pk", updatable = false)
    private Long categoryPk;

    @Column(name = "cate_name", nullable = false, length = 50)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<GoodsCategoriesMappingEntity> goods;
}
