package Try_it.goods;

import Try_it.order.OrderListEntity;
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
        """)
    Page<GoodsEntity> findAllByKeyword(@Param("keyword") String keyword,
                                       Pageable pageable);

    @Query("""
        SELECT g FROM GoodsEntity g
        LEFT JOIN g.category gc
        LEFT JOIN gc.category c
        WHERE (:gender IS NULL OR :gender = '' OR c.categoryName = :gender)
        AND (:isChild IS NULL OR :isChild = false OR c.categoryName = '아동')
        AND (:category IS NULL OR :category = '' OR c.categoryName = :category)
        AND (LOWER(g.goodsName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR :keyword = '')
        """)
    Page<GoodsEntity> findAllBySwimmerFilter(@Param("gender") String gender,
                                       @Param("isChild") Boolean isChild,
                                       @Param("category") String category,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);

    @Query("""
        SELECT g FROM GoodsEntity g
        LEFT JOIN g.category gc
        LEFT JOIN gc.category c
        WHERE c.categoryName = :category 
        AND LOWER(g.goodsName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    Page<GoodsEntity> findAllBySuppliesFilter(@Param("category") String category,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);
//    수영복을 선택할 경우
//
//?bigCategory=수영복&gender=(여성|남성)&isChild=true&category=원피스
//
//    용품을 선택할 경우
//
//?bigCategory=용품&isChild=true&category=패킹

    GoodsEntity findByOrderList(OrderListEntity orderList);
}
