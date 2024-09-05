package Try_it.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@org.springframework.stereotype.Controller
@RequestMapping("/pages")
public class Controller {
    @GetMapping("/login")
    public String loginPage(Model model){return "login";}

    @GetMapping("/main")
    public String mainPage(Model model){return "main";}

    @GetMapping("/coupon")
    public String couponListPage(Model model){return "coupon";}
}
