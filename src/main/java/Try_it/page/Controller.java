package Try_it.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@org.springframework.stereotype.Controller
@RequestMapping("/pages")
public class Controller {
    @GetMapping("/login")
    public String loginPage(Model model){return "login";}

    @GetMapping("/main")
    public String mainPage(Model model){
        return "main";
    }

    @GetMapping("/coupon")
    public String couponListPage(Model model){return "coupon";}

    @GetMapping("/orders")
    public String orderPage(Model model){return "order";}

    @GetMapping("/payment")
    public String payPage(Model model){return "payTest";}

    @GetMapping("/goodsDetail")
    public String goodsDetailPage(Model model){return "goodsDetail";}
}
