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
    public ResponseEntity<ResDTO<Object>> getGoodsList(@Parameter(name = "page", description = "현재 페이지 번호", in = ParameterIn.QUERY, example = "1")
                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                           @Parameter(name = "sort", description = "정렬 기준", in = ParameterIn.QUERY, example = "goodsName")
                                               @RequestParam(value = "sort", defaultValue = "goodsName") String sort,
                                           @Parameter(name = "direction", description = "정렬 순서", in = ParameterIn.QUERY, example = "desc")
                                               @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                           @Parameter(name = "keyword", description = "검색어", in = ParameterIn.QUERY, example = "수영복")
                                               @RequestParam(value = "keyword", required = false) String keyword){

        Page<GoodsEntity> goods = goodsService.getGoodsList(page, sort, direction, keyword);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .message("상품 조회 성공")
           .data(goods)
           .build());
    }

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
