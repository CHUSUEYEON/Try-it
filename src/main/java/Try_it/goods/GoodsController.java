package Try_it.goods;

import Try_it.common.dto.ResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/goods")
@Tag(name = "Goods", description = "상품 관련 API")
public class GoodsController {
    final private GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Operation(summary = "상품 등록", description = "requestbody : 상품명, 설명, 가격, 사진 / 관리자 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
        @ApiResponse(responseCode = "400", description = "상품 등록 실패")
    })
    @PostMapping("")
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
           .statusCode(200)
           .data(responseGoodsDTO)
           .message("상품 등록 성공")
           .build());
    }

    @Operation(summary = "상품 수정", description = "requestbody : 상품명, 설명, 가격, 사진(필수 아님) / 관리자 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "400", description = "상품 수정 실패")
    })
    @PatchMapping("/{goodsIdx}")
    public ResponseEntity<ResDTO<Object>> updateGoods(@Valid @RequestPart GoodsDTO goodsDTO,
                                              @RequestPart(required = false) MultipartFile file,
                                              @PathVariable Long goodsIdx,
                                              @AuthenticationPrincipal String userIdx
                                              ) throws Exception{
        GoodsEntity updatedGoods = goodsService.updateGoods(goodsDTO, file, goodsIdx, userIdx);

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(200)
           .data(updatedGoods)
            .message("상품 수정 성공")
            .build());
    }

}
