package Try_it.goods;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

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

    @Column(name = "g_file", nullable = false)
    private String goodsFile;

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
}
