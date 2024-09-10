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
@Tag(name = "Goods", description = "인가 필요없는 상품 관련 API")
@Slf4j
public class GoodsController {

    final private GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Operation(summary = "상품 조회", description = "keyword로 검색 가능")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
        @ApiResponse(responseCode = "400", description = "상품 조회 실패")
    })
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
        Page<GoodsEntity> goods = goodsService.getGoodsList(page, sort, direction, bigCategory, gender, isChild, category, keyword);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .message("상품 조회 성공")
           .data(goods)
           .build());
    }

    @Operation(summary = "상품 상세 조회", description = "path : goodsPk")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
        @ApiResponse(responseCode = "400", description = "상품 조회 실패")
    })
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
