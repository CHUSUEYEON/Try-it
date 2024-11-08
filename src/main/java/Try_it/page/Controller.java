package Try_it.page;

import Try_it.cart.CartService;
import Try_it.order.OrderDTO;
import Try_it.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@org.springframework.stereotype.Controller
@RequestMapping("/pages")
@Slf4j
public class Controller {
    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public Controller(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
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
    public String payPage(@AuthenticationPrincipal String userPk,
                          Model model){
        System.out.println("userPk2222 = " + userPk);
        // (2) TODO: modelAttribute 이용하여 View에 session에 담긴 주문 정보 보내기
        List<OrderDTO> tempOrders = orderService.createCartsTempOrder(userPk);
        tempOrders.forEach(entity -> log.info("Entity: {}", entity.getGoods().toString()));
        model.addAttribute("tempOrders", tempOrders);

        AtomicInteger totalPrice = new AtomicInteger();

         tempOrders.forEach(entity -> totalPrice.addAndGet(entity.getOrderTotal()));
         model.addAttribute("totalPrice", totalPrice);
//        tempOrders.forEach(entity -> model.addAttribute("orderQuantity", entity.getOrderQuantity()));
        return "payTest";
    }

    @GetMapping("/goodsDetail")
    public String goodsDetailPage(Model model){return "goodsDetail";}

//    @GetMapping("/cart")
//    public String cartPage(Model model){return "cart";}

    @GetMapping("/cart")
    public String getCarts(@AuthenticationPrincipal String userPk,
//                           @RequestParam(value = "page", defaultValue = "0") Integer page,
//                           @RequestParam(value = "sort", defaultValue = "cartCreatedAt") String sort,
//                           @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                           Model model
    ){
        System.out.println("userPk = " + userPk);
//        Page<CartEntity> carts = cartService.getCarts(page, sort, direction, userPk);
//        System.out.println("Carts: {}" + carts.getContent());
//        model.addAttribute("carts", carts.getContent()); // 장바구니 목록
//        model.addAttribute("totalPrice", carts.getContent().stream()
//            .mapToLong(cart -> cart.getGoods().getGoodsPrice() * cart.getGoods))
        return "cart";
    }
}
