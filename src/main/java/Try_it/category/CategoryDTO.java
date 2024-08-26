package Try_it.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "카테고리")
public class CategoryDTO {
    @Schema(description = "카테고리 인덱스", example = "1")
    private Long categoryPk;

    @Schema(description = "카테고리 이름", example = "여성")
    private String categoryName;
}
