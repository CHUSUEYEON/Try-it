package Try_it.goods;

import Try_it.common.dto.ResDTO;
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

}
