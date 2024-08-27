package Try_it.review;

import Try_it.goods.GoodsEntity;
import Try_it.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "리뷰 정보")
public class ReviewDTO {
    @Schema(description = "리뷰 인덱스", example = "1")
    private Long reviewPk;

    @Schema(description = "회원 내용", example = "리뷰입니다.")
    private String reviewContent;

    @Schema(description = "별점", example = "1")
    @Min(1)
    @Max(5)
    private Integer reviewRate;

    @Schema(description = "리뷰 이미지 수", example = "1")
    private Integer reviewFile;

    @Schema(description = "회원 생성일자", example = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp reviewCreatedAt;

    @Schema(description = "회원 수정일자", example = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", nullable = true)
    private Timestamp reviewUpdatedAt;

    @Schema(description = "회원 삭제일자, 탈퇴한 회원이면 타임스탬프, 아니면 Null", example = "null", nullable = true)
    private Timestamp reviewDeletedAt;
}
