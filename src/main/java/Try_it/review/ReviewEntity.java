package Try_it.review;

import Try_it.goods.entity.GoodsEntity;
import Try_it.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@SQLDelete(sql = "UPDATE review SET r_deleted_at = now() WHERE r_idx = ?")
@Table(name = "review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_idx", updatable = false)
    private Long reviewIdx;

    @Column(name = "r_content", nullable = false)
    private String reviewContent;

    @Column(name = "r_rate", nullable = false)
    private Integer reviewRate;

    @Column(name = "r_file", nullable = false)
    private String reviewFile;

    @Column(name = "r_created_at", nullable = false)
    @CreationTimestamp
    private Timestamp reviewCreatedAt;

    @Column(name = "r_updated_at")
    @UpdateTimestamp
    private Timestamp reviewUpdatedAt;

    @Column(name = "r_deleted_at")
    private Timestamp reviewDeletedAt;

    @ManyToOne
    @JoinColumn(name = "u_idx", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "g_idx", nullable = false)
    @JsonBackReference
    private GoodsEntity goods;

}
