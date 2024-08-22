package Try_it.goods;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import Try_it.goods.entity.GoodsEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Goods", description = "상품 관련 API")
@Slf4j
public class AdminGoodsController {
    final private AdminGoodsService goodsService;

    @Autowired
    public AdminGoodsController(AdminGoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Operation(summary = "상품 등록", description = "requestbody : 상품명, 설명, 가격, 사진 / 관리자 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
        @ApiResponse(responseCode = "400", description = "상품 등록 실패")
    })
    @PostMapping("/goods")
    public ResponseEntity<ResDTO> uploadGoods(@Valid @RequestPart GoodsDTO goodsDTO,
                                              @RequestPart MultipartFile file,
                                              @AuthenticationPrincipal String userIdx
                                              ) throws Exception{
        GoodsEntity uploadedGoods = goodsService.createGoods(goodsDTO, file, userIdx);
        GoodsDTO responseGoodsDTO = goodsDTO.builder()
            .goodsName(uploadedGoods.getGoodsName())
            .goodsDescription(uploadedGoods.getGoodsDescription())
            .goodsIdx(uploadedGoods.getGoodsIdx())
            .goodsFile(uploadedGoods.getGoodsFile())
            .goodsCreatedAt(uploadedGoods.getGoodsCreatedAt())
            .goodsUpdatedAt(uploadedGoods.getGoodsUpdatedAt())
            .goodsDeletedAt(uploadedGoods.getGoodsDeletedAt())
            .goodsPrice(uploadedGoods.getGoodsPrice())
            .build();

        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(responseGoodsDTO)
           .message("상품 등록 성공")
           .build());
    }

    @Operation(summary = "상품 수정", description = "requestbody : 상품명, 설명, 가격, 사진(필수 아님) / 관리자 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "400", description = "상품 수정 실패")
    })
    @PatchMapping("/goods/{goodsIdx}")
    public ResponseEntity<ResDTO<Object>> updateGoods(@Valid @RequestPart GoodsDTO goodsDTO,
                                              @RequestPart(required = false) MultipartFile file,
                                              @PathVariable Long goodsIdx,
                                              @AuthenticationPrincipal String userIdx
                                              ) throws Exception{
        GoodsEntity updatedGoods = goodsService.updateGoods(goodsDTO, file, goodsIdx, userIdx);

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
           .data(updatedGoods)
            .message("상품 수정 성공")
            .build());
    }

    @Operation(summary = "상품 삭제", description = "관리자 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "상품 삭제 실패")
    })
    @DeleteMapping("/goods/{goodsIdx}")
    public ResponseEntity<ResDTO> deleteGoods(@PathVariable Long goodsIdx, @AuthenticationPrincipal String userIdx){
        GoodsEntity deletedGoods = goodsService.delete(goodsIdx, userIdx);
        return ResponseEntity.ok().body(ResDTO
            .builder()
           .statusCode(StatusCode.OK)
            .data(deletedGoods)
           .message("상품 삭제 성공")
           .build());
    }

//    @Operation(summary = "상품 조회", description = "관리자 토큰 필요, keywords로 검색 가능")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
//        @ApiResponse(responseCode = "400", description = "상품 조회 실패")
//    })
//    @GetMapping("/goods")
//    public ResponseEntity<ResDTO> getGoodsList(@Parameter(name = "page", description = "현재 페이지 번호", in = ParameterIn.QUERY, example = "1")
//                                            @RequestParam(name = "page", required = false) Integer page,
//                                            @Parameter(name = "page", description = "정렬 기준", in = ParameterIn.QUERY, example = "name")
//                                            @RequestParam(name = "page", required = false) String sort,
//                                            @Parameter(name = "direction", description = "정렬 순서", in = ParameterIn.QUERY, example = "desc")
//                                            @RequestParam(name = "direction", required = false) String direction,
//                                            @Parameter(name = "keyword", description = "검색어", in = ParameterIn.QUERY, example = "수영복")
//                                            @RequestParam(required = false) String keyword,
//                                           @Parameter(name = "filter", description = "필터링 조건", in = ParameterIn.QUERY)
//                                           @ModelAttribute GoodsFilterDTO filter,
//                                           @AuthenticationPrincipal String userIdx) {
//        if (keyword == null || keyword.isEmpty()) {
//            if(page == null) page = 1;
//            Page<GoodsDTO> goods = goodsService.getGoodsList(page, sort, direction, filter, userIdx);
//            return ResponseEntity.ok().body(ResDTO
//                .builder()
//                .statusCode(StatusCode.OK)
//                .data(goods)
//                .message("상품 조회 성공")
//                .build());
//        } else {
//            List<GoodsEntity> goods = goodsService.getGoods(keyword, userIdx);
//            return ResponseEntity.ok().body(ResDTO
//                .builder()
//                .statusCode(StatusCode.OK)
//                .data(goods)
//                .message("상품 조회 성공")
//                .build());
//        }
//    }
    }



