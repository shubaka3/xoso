package hutech.com.demo.Controller;

import hutech.com.demo.VNPAY.VNPAYService;
import hutech.com.demo.model.CartItem;
import hutech.com.demo.model.Order;
import hutech.com.demo.model.OrderDetail;
import hutech.com.demo.repository.OrderDetailRepository;
import hutech.com.demo.repository.OrderRepository;
import hutech.com.demo.service.CartService;
import hutech.com.demo.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private VNPAYService vnPayService;
    @GetMapping("/checkout")
    public String checkout() {
        return "/cart/checkout";
    }
    @PostMapping("/submit")
    public String submitOrder(String address, String phone, String email, String note, String payment, HttpServletRequest request,@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }
        String customerName = username;
        Order order = orderService.createOrder(customerName,address,phone,email,note,payment, cartItems);

        //VNPAY
        Integer orderTotal = order.getTotalPrice().intValue();
        String orderInfo = order.getNote();
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }
    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }
    @PostMapping("/updateQuantity")
    public String updateCartItemQuantity(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateCartItemQuantity(productId, quantity);
        return "redirect:/cart";
    }
    @GetMapping("")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
    @GetMapping("/list")
    public String getAllUserOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "cart/orders"; // Trả về view để hiển thị danh sách đơn hàng
    }

    @GetMapping("/list/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, Model model) {
        Optional<Order> order = Optional.ofNullable(orderService.getOrderById(orderId));
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            model.addAttribute("orderDetails", order.get().getOrderDetails());
            return "cart/order-details"; // Trả về view để hiển thị chi tiết đơn hàng
        } else {
            return "redirect:/cart/order"; // Chuyển hướng nếu không tìm thấy đơn hàng
        }
    }
}
