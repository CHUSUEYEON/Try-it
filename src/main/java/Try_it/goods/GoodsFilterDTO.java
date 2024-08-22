package Try_it.goods;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
@Schema(name = "SearchDTO", description = "검색에 필요한 쿼리스트링 관련 DTO")
public class GoodsFilterDTO {
    @Schema(name="name", example = "딸기수영복")
    private String name;

    @Schema(name="file", description="사진 경로", example = "http://dsljklj.cloudfront.net/asdf")
    private List<String> file;

    @Schema(name = "description", example = "딸기 수영복입니다.")
    private List<String> description;

    @Schema(name = "price", example = "10000")
    private List<Integer> price;

    @Schema(name = "category", example = "원피스")
    private List<String> category;

    @QueryProjection
    public GoodsFilterDTO(String name, List<String> file, List<String> description, List<Integer> price, List<String> category) {
        this.name = name;
        this.file = file;
        this.description = description;
        this.price = price;
        this.category = category;
    }
}
