package Try_it.goods;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
                                     final String keyword){

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortDirection, sort));

        if(keyword == null || keyword.isEmpty()){
            Page<GoodsEntity> goods = goodsRepository.findAll(pageable);
            if(goods.isEmpty()){
                throw new IllegalStateException("등록된 상품이 없습니다.");
            }
            return goods;
        } else{
            Page<GoodsEntity> goods = goodsRepository.findAllByKeyword(keyword, pageable);
            if(goods.isEmpty()){
                throw new IllegalStateException("등록된 상품이 없습니다.");
            }
            return goods;
        }
    }

    public GoodsEntity getGoods(final Long goodsPk){
        GoodsEntity goods = goodsRepository.findById(goodsPk).orElseThrow(() ->
            new IllegalStateException("해당되는 상품이 없습니다."));
        return goods;
    }
}
