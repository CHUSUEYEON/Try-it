package Try_it.goods;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
@Tag(name = "Auth X", description = "인가 필요없는 API")
@Slf4j
public class GoodsController {

}
