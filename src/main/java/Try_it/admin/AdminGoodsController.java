package Try_it.admin;

import Try_it.category.CategoryDTO;
import Try_it.common.dto.ResDTO;
import Try_it.common.util.FileUpload;
import Try_it.common.vo.StatusCode;
import Try_it.goods.GoodsDTO;
import Try_it.goods.GoodsEntity;
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
@RequestMapping("/admin/goods")
@Tag(name = "Goods", description = "상품 관련 API")
@Slf4j
public class AdminGoodsController {
    final private AdminGoodsService goodsService;
    private final FileUpload fileUpload;

    @Autowired
    public AdminGoodsController(AdminGoodsService goodsService, FileUpload fileUpload) {
        this.goodsService = goodsService;
        this.fileUpload = fileUpload;
    }

    @Operation(summary = "상품 등록", description = "requestbody : 상품명, 설명, 가격, 사진 / 관리자 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
        @ApiResponse(responseCode = "400", description = "상품 등록 실패")
    })
    @PostMapping("")
    public ResponseEntity<ResDTO> uploadGoods(@Valid @RequestPart GoodsDTO goodsDTO,
                                              @RequestPart List<CategoryDTO> categoryDTOs,
                                              @RequestPart List<MultipartFile> files,
                                              @AuthenticationPrincipal String userPk
                                              ) throws Exception{
        GoodsEntity uploadedGoods = goodsService.createGoods(goodsDTO, categoryDTOs, files, userPk);
        GoodsDTO responseGoodsDTO = goodsDTO.builder()
            .goodsName(uploadedGoods.getGoodsName())
            .goodsDescription(uploadedGoods.getGoodsDescription())
            .goodsPk(uploadedGoods.getGoodsPk())
            .goodsImgCount(uploadedGoods.getGoodsImgCount())
            .goodsCreatedAt(uploadedGoods.getGoodsCreatedAt())
            .goodsUpdatedAt(uploadedGoods.getGoodsUpdatedAt())
            .goodsDeletedAt(uploadedGoods.getGoodsDeletedAt())
            .goodsPrice(uploadedGoods.getGoodsPrice())
            .categoryName(uploadedGoods.getCategory())
            .build();
        List<String> fileNames = fileUpload.generateFileName(responseGoodsDTO, files);
        fileUpload.uploadFile(files, fileNames);
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
    @PatchMapping("/{goodsPk}")
    public ResponseEntity<ResDTO<Object>> updateGoods(@Valid @RequestPart(required = false) GoodsDTO goodsDTO,
                                              @RequestPart(required = false) List<CategoryDTO> categoryDTOs,
                                              @RequestPart(required = false) List<MultipartFile> files,
                                              @PathVariable Long goodsPk,
                                              @AuthenticationPrincipal String userPk
                                              ) throws Exception{
        GoodsEntity updatedGoods = goodsService.updateGoods(goodsDTO, categoryDTOs, files, goodsPk, userPk);

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
    @DeleteMapping("/{goodsPk}")
    public ResponseEntity<ResDTO> deleteGoods(@PathVariable Long goodsPk, @AuthenticationPrincipal String userPk){
        GoodsEntity deletedGoods = goodsService.delete(goodsPk, userPk);
        return ResponseEntity.ok().body(ResDTO
            .builder()
           .statusCode(StatusCode.OK)
            .data(deletedGoods)
           .message("상품 삭제 성공")
           .build());
    }

    @Operation(summary = "상품 조회", description = "관리자 토큰 필요, keywords로 검색 가능")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
        @ApiResponse(responseCode = "400", description = "상품 조회 실패")
    })
    @GetMapping("")
    public ResponseEntity<ResDTO<Object>> getGoodsList(@Parameter(name = "page", description = "현재 페이지 번호", in = ParameterIn.QUERY, example = "1")
                                            @RequestParam(value = "page", defaultValue = "0") Integer page,
                                            @Parameter(name = "page", description = "정렬 기준", in = ParameterIn.QUERY, example = "name")
                                            @RequestParam(value = "sort", defaultValue = "goodsName") String sort,
                                            @Parameter(name = "direction", description = "정렬 순서", in = ParameterIn.QUERY, example = "desc")
                                            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                            @Parameter(name = "keyword", description = "검색어", in = ParameterIn.QUERY, example = "수영복")
                                            @RequestParam(value = "keyword", required = false) String keyword,
                                           @AuthenticationPrincipal String userPk) {

            Page<GoodsEntity> goods = goodsService.getGoodsList(page, sort, direction, userPk, keyword);
            return ResponseEntity.ok().body(ResDTO
                .builder()
                .statusCode(StatusCode.OK)
                .data(goods)
                .message("상품 조회 성공")
                .build());
    }


    @Operation(summary = "상품 조회", description = "관리자 토큰 필요 / path 로 해당 상품 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
        @ApiResponse(responseCode = "400", description = "상품 조회 실패")
    })
    @GetMapping("/{goodsPk}")
    public  ResponseEntity<ResDTO> getGoods(@AuthenticationPrincipal String userPk, @PathVariable Long goodsPk){
        GoodsEntity goods = goodsService.getGoods(goodsPk, userPk);
        return ResponseEntity.ok().body(ResDTO
           .builder()
           .statusCode(StatusCode.OK)
           .data(goods)
           .message("상품 조회 성공")
           .build());
    }
    }



