package Try_it.goods;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "상품 정보")
public class GoodsDTO {
    @Schema(description = "상품 인덱스", example = "1")
    private Long goodsPk;

    @Schema(description = "상품 이름", example = "수영복")
    private String goodsName;

    @Schema(description = "상품 개수", example = "5")
    private Integer goodsImgCount;

    @Schema(description = "상품 설명", example = "수영복입니다")
    private String goodsDescription;

    @Schema(description = "상품 가격", example = "10000")
    private Integer goodsPrice;

    @Schema(description = "상품 생성일자", example = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp goodsCreatedAt;

    @Schema(description = "상품 수정일자", example = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", nullable = true)
    private Timestamp goodsUpdatedAt;

    @Schema(description = "상품 삭제일자", example = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", nullable = true)
    private Timestamp goodsDeletedAt;

    @Schema(description = "상품 카테고리", example = "원피스")
    private List categoryName;

    public GoodsDTO(Long goodsPk, String goodsName, Integer goodsImgCount, String goodsDescription, Integer goodsPrice, Timestamp goodsCreatedAt, Timestamp goodsUpdatedAt, Timestamp goodsDeletedAt, List categoryName) {
        this.goodsPk = goodsPk;
        this.goodsName = goodsName;
        this.goodsImgCount = goodsImgCount;
        this.goodsDescription = goodsDescription;
        this.goodsPrice = goodsPrice;
        this.goodsCreatedAt = goodsCreatedAt;
        this.goodsUpdatedAt = goodsUpdatedAt;
        this.goodsDeletedAt = goodsDeletedAt;
        this.categoryName = categoryName;
    }
}
