package Try_it.goods;

import Try_it.order.OrderListEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query(
        nativeQuery = true,
        value = "SELECT g.*, GROUP_CONCAT(c.cate_name SEPARATOR ', ') AS categories " +
            "FROM goods g " +
            "LEFT JOIN goods_cate_mapping gm ON g.g_pk = gm.g_pk " +
            "LEFT JOIN category c ON gm.cate_pk = c.cate_pk " +
            "GROUP BY g.g_pk " +
        "HAVING (categories LIKE CONCAT('%', :gender,'%') AND categories LIKE CONCAT('%', :category, '%') AND :isChild = false OR categories LIKE '%아동%')" +
        "AND g.g_name LIKE CONCAT('%', :keyword, '%')"
//        countQuery = "SELECT COUNT(*) FROM goods"
    )
    Page<GoodsEntity> findAllBySwimmerFilter(
        @Param("keyword") String keyword,
        @Param("gender") String gender,
        @Param("category") String category,
        @Param("isChild") boolean isChild,
        Pageable pageable
    );

    @Query(
        nativeQuery = true,
        value = "SELECT g.*, GROUP_CONCAT(c.cate_name SEPARATOR ', ') AS categories " +
            "FROM goods g " +
            "LEFT JOIN goods_cate_mapping gm ON g.g_pk = gm.g_pk " +
            "LEFT JOIN category c ON gm.cate_pk = c.cate_pk " +
            "GROUP BY g.g_pk " +
            "HAVING (categories LIKE CONCAT('%', :category, '%') AND :isChild = false OR categories LIKE '%아동%')" +
            "AND g.g_name LIKE CONCAT('%', :keyword, '%')"
//        countQuery = "SELECT COUNT(*) FROM goods"
    )
    Page<GoodsEntity> findAllBySuppliesFilter(@Param("keyword") String keyword,
                                       @Param("category") String category,
                                       @Param("isChild") boolean isChild,
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
// 필터링 Todo: bigCategory로 필터링이 안 됨.
//  ex) 상품 1번의 카테고리 = [남성, 패킹, 아동]
// 상품 2번의 카테고리 = [아동, 탄탄이]
// 그럼 bigCategory 가 용품일 경우 1번 상품만 조회되어야 하는데, bigCategory=용품, isChild=true 로 값을 넣고 조회하면 둘 다 조회됨.
