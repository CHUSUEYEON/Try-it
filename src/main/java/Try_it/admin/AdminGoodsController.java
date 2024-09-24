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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/admin/goods")
@Tag(name = "Admin Goods", description = "관리자가 관리하는 상품 관련 API")
@Slf4j
public class AdminGoodsController {
    final private AdminGoodsService goodsService;
    private final FileUpload fileUpload;

    @Autowired
    public AdminGoodsController(AdminGoodsService goodsService, FileUpload fileUpload) {
        this.goodsService = goodsService;
        this.fileUpload = fileUpload;
    }

    @Operation(summary = "상품 등록", description = """
            테스트 방법: 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            인증/인가 : 관리자 로그인 필요\s
            파일 첨부 시 [Add String item] 클릭하여 가능(선택)\s
            \s
            goodsDTO : { "goodsName": "수영복", "goodsDescription": "수영복입니다.", "goodsPrice": 10000 }\s
            categoryDTOs : [{ "categoryName" : "여성" }, { "categoryName" : "원피스" }]\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
    @PostMapping(value = "", consumes = MULTIPART_FORM_DATA_VALUE)
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
        List<String> fileNames = fileUpload.generateFileName(responseGoodsDTO.getGoodsPk(), files);
        fileUpload.uploadFile(files, fileNames);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(responseGoodsDTO)
           .message("상품 등록 성공")
           .build());
    }

    @Operation(summary = "상품 수정",  description = """
            테스트 방법: 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            인증/인가 : 관리자 로그인 필요\s
            파일 첨부 시 [Add String item] 클릭하여 가능(선택)\s
            \s
            goodsDTO : { "goodsName": "상품 변경 테스트", "goodsDescription": "변경 테스트입니다.", "goodsPrice": 9000 }\s
            categoryDTOs : [{ "categoryName" : "남성" }]\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
    @PatchMapping(value = "/{goodsPk}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @Operation(summary = "상품 삭제", description = """
            goodsPk : 4  \s
            인증/인가 : 관리자 로그인 필요 \s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
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

    @Operation(summary = "상품 조회", description = "관리자 토큰 필요, keywords로 검색 가능(필수 아님, 키워드 없으면 전체 검색)")
    @GetMapping("")
    public ResponseEntity<ResDTO<Object>> getGoodsList(@Parameter(name = "page", description = "현재 페이지 번호", in = ParameterIn.QUERY, example = "0")
                                            @RequestParam(value = "page", defaultValue = "0") Integer page,
                                            @Parameter(name = "sort", description = "정렬 기준", in = ParameterIn.QUERY, example = "goodsName")
                                            @RequestParam(value = "sort", defaultValue = "goodsName") String sort,
                                            @Parameter(name = "direction", description = "정렬 순서", in = ParameterIn.QUERY, example = "ASC")
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


    @Operation(summary = "상품 조회", description = """
            goodsPk : 4  \s
            인증/인가 : 관리자 로그인 필요 \s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
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



