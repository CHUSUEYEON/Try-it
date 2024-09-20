package Try_it.goods;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
@Tag(name = "Goods", description = "인가 필요없는 상품 관련 API(토큰 필요없습니다.)")
@Slf4j
public class GoodsController {

    final private GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Operation(summary = "상품 조회", description = "필터링 >>> bigCategory : 수영복, 용품 / gender : 여성, 남성 / isChild : true, false/ category : 원피스, 탄탄이, 패킹, 노패킹, 실리콘, 메쉬 /keyword로 검색 가능 / 아무것도 설정 안할 경우 전체 조회")
    @GetMapping("")
    public ResponseEntity<ResDTO<Object>> getGoodsList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "sort", defaultValue = "goodsName") String sort,
                                               @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                @RequestParam(value = "bigCategory", required = false) String bigCategory,
                                               @RequestParam(value = "gender", required = false) String gender,
                                                @RequestParam(value = "isChild", required = false) boolean isChild,
                                                @RequestParam(value = "category", required = false) String category,
                                               @RequestParam(value = "keyword", required = false) String keyword){
log.warn("ddddd {} {} {} {} {} {} {} {}",page, sort, direction, bigCategory, gender, isChild, category, keyword);
        String safeKeyword = (keyword != null) ? keyword : "";
        String safeGender = (gender != null) ? gender : "";
        String safeCategory = (category != null) ? category : "";
        String safeBigCategory = (bigCategory != null) ? bigCategory : "";
        log.warn("ㅋㅋㅋㅋ{} {} {} {}", safeGender, safeCategory, safeKeyword, safeBigCategory);

        Page<GoodsEntity> goods = goodsService.getGoodsList(page, sort, direction, bigCategory, safeGender, isChild, safeCategory, safeKeyword);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .message("상품 조회 성공")
           .data(goods)
           .build());
    }

    @Operation(summary = "상품 상세 조회", description = "path : goodsPk")
    @GetMapping("/{goodsPk}")
    public ResponseEntity<ResDTO<Object>> getGoods(@PathVariable("goodsPk") Long goodsPk){
        GoodsEntity goods = goodsService.getGoods(goodsPk);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .message("상품 상세 조회 성공")
           .data(goods)
           .build());
    }
}
