package Try_it.page;

import Try_it.cart.CartEntity;
import Try_it.cart.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@org.springframework.stereotype.Controller
@RequestMapping("/pages")
public class Controller {
    private final CartService cartService;

    @Autowired
    public Controller(CartService cartService) {
        this.cartService = cartService;
    }

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

//    @GetMapping("/cart")
//    public String cartPage(Model model){return "cart";}

    @GetMapping("/cart")
    public String getCarts(@AuthenticationPrincipal String userPk,
                           @RequestParam(value = "page", defaultValue = "0") Integer page,
                           @RequestParam(value = "sort", defaultValue = "cartCreatedAt") String sort,
                           @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                           Model model
    ){
        System.out.println("aaaaaaaa");
        System.out.println("userPk = " + userPk);
        Page<CartEntity> carts = cartService.getCarts(page, sort, direction, userPk);
        System.out.println("aaaaaaaa");
//        System.out.println("Carts: {}" + carts.getContent());
//        model.addAttribute("carts", carts.getContent()); // 장바구니 목록
//        model.addAttribute("totalPrice", carts.getContent().stream()
//            .mapToLong(cart -> cart.getGoods().getGoodsPrice() * cart.getGoods))
        return "cart";
    }
}
