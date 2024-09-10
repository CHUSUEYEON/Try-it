package Try_it.goods;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GoodsService {
    final private GoodsRepository goodsRepository;

    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public Page<GoodsEntity> getGoodsList(final int page,
                                          final String sort,
                                          final String direction,
                                          final String bigCategory,
                                          final String gender,
                                          final boolean isChild,
                                          final String category,
                                          final String keyword){

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        Page<GoodsEntity> goods;

        String where = "";

        if(gender != null && !gender.isEmpty()) {
            where += "categories LIKE CONCAT('%', " + gender + ", '%')";
        }

        if(isChild) {
            if(!where.isEmpty()) where += " OR ";
            where += "categories like '%아동%'";
        }

        if(category != null && !category.isEmpty()) {
            if(!where.isEmpty()) where += " OR ";
            where += "categories LIKE CONCAT('%', " + category + ", '%')";
        }

        log.warn("Category {}", where);

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortDirection, sort));

        switch (bigCategory){
            case "수영복" :
                pageable = PageRequest.of(page, 10, Sort.by(sortDirection, "g_name"));
                goods = goodsRepository.findAllBySwimmerFilter(
                    keyword !=  null ? keyword: "",
                    gender !=null ? gender : "",
                    category !=null ? category :  "",
                    isChild,
                    pageable);
                break;
            case "용품" :
                pageable = PageRequest.of(page, 10, Sort.by(sortDirection, "g_name"));
                goods = goodsRepository.findAllBySuppliesFilter(
                    keyword !=  null ? keyword: "",
                    category !=null ? category :  "",
                    isChild,
                    pageable);
                break;
            default:
                pageable = PageRequest.of(page, 10, Sort.by(sortDirection, "g_name"));
                goods = goodsRepository.findAllByKeyword(
                    keyword !=  null ? keyword: "",
                    pageable);
        }

        if(goods.isEmpty()){
            throw new IllegalStateException("등록된 상품이 없습니다.");
        }
        return goods;
    }

//    public List<GoodsEntity> getGoodsList(final int page,
//                                     final String sort,
//                                     final String direction,
//                                     final String bigCategory,
//                                     final String gender,
//                                     final boolean isChild,
//                                     final String category,
//                                     final String keyword){
//
//        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
//
////        if(keyword == null || keyword.isEmpty()){
////            Page<GoodsEntity> goods = goodsRepository.findAll(pageable);
////            if(goods.isEmpty()){
////                throw new IllegalStateException("등록된 상품이 없습니다.");
////            }
////            return goods;
////        } else{
//            List<GoodsEntity> goods;
//
////            String where = "";
////
////            if(gender != null && !gender.isEmpty()) {
////                where += "categories LIKE CONCAT('%', " + gender + ", '%')";
////            }
////
////            if(isChild) {
////                if(!where.isEmpty()) where += " OR ";
////                where += "categories like '%아동%'";
////            }
////
////            if(category != null && !category.isEmpty()) {
////                if(!where.isEmpty()) where += " OR ";
////                where += "categories LIKE CONCAT('%', " + category + ", '%')";
////            }
//
////            log.warn("Category {}", where);
//
////            Pageable  pageable = PageRequest.of(page, 10, Sort.by(sortDirection, "g_name"));
//            goods = goodsRepository.findAllBySwimmerFilter(
//                keyword !=  null ? keyword: "",
//                gender !=null ? gender : "",
//                category !=null ? category :  "");
//
//            if(goods.isEmpty()){
//                throw new IllegalStateException("등록된 상품이 없습니다.");
//            }
//            return goods;
////        }
//    }

    public GoodsEntity getGoods(final Long goodsPk){
        GoodsEntity goods = goodsRepository.findById(goodsPk).orElseThrow(() ->
            new IllegalStateException("해당되는 상품이 없습니다."));
        return goods;
    }
}
