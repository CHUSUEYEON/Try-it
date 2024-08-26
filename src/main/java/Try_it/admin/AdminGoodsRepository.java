package Try_it.admin;

import Try_it.goods.entity.GoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminGoodsRepository extends JpaRepository<GoodsEntity, Long>{

//    @Query("SELECT g FROM GoodsEntity g WHERE g.goodsName Like %:keyword%")
//    List<GoodsEntity> findGoodsByKeyword(@Param("keyword") String keyword);

//    Page<GoodsDTO> getGoodsList(Pageable pageable, String sort, String direction);

    @Query("""
        SELECT g FROM GoodsEntity g
        LEFT JOIN g.category gc
        LEFT JOIN gc.category c
        WHERE LOWER(g.goodsName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    Page<GoodsEntity> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
