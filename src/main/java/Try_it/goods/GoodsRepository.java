package Try_it.goods;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {
    @Query("""
        SELECT g FROM GoodsEntity g
        LEFT JOIN g.category gc
        LEFT JOIN gc.category c
        WHERE LOWER(g.goodsName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    Page<GoodsEntity> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
