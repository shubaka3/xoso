package hutech.com.demo.Controller;

import hutech.com.demo.model.Number;
import hutech.com.demo.model.OrderDetail;
import hutech.com.demo.model.User;
import hutech.com.demo.service.NumberService;
import hutech.com.demo.service.OrderService;
import hutech.com.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller // Đánh dấu lớp này là một Controller trong Spring MVC.
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private final NumberService numberService;
    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vàomodel
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, // Validate đối tượng User
                           @NotNull BindingResult bindingResult, // Kết quả của quátrình validate
                           Model model) {
        if (bindingResult.hasErrors()) { // Kiểm tra nếu có lỗi validate
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register"; // Trả về lại view "register" nếu có lỗi
        }
        userService.save(user); // Lưu người dùng vào cơ sở dữ liệu
        userService.setDefaultRole(user.getUsername()); // Gán vai trò mặc định cho người dùng
        return "redirect:/login"; // Chuyển hướng người dùng tới trang "login"
    }
    @GetMapping("/user-id")
    @ResponseBody
    public Long getUserIdFromDatabase(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getId();
        }
        return null;
    }
    @GetMapping("/user-profile")
    public String getUserProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = getUserIdFromDatabase(userDetails); // Use userDetails directly
            model.addAttribute("userId", userId);
        }
        return "users/UserDetail"; // Your Thymeleaf template name
    }

    @GetMapping("/userhistory")
    public String getAllOrders(Model model,@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<Number> numbers = numberService.getAllNumbers();
        List<OrderDetail> allOrders = orderService.getAllOrderDetail();
        List<OrderDetail> filteredOrders = new ArrayList<>();
        List<OrderDetail> filteredOrdersa = new ArrayList<>();


        for (OrderDetail order : allOrders) {
            if (username.equals(order.getOrder().getCustomerName())) {
                filteredOrders.add(order);
//                String productName = order.getProduct().getName();
//                if (numbers.stream().anyMatch(number -> number.getValuenumber().toString().equals(productName))) {
//                    filteredOrdersa.add(order);
//                }
            }
        }
        model.addAttribute("order", filteredOrders.getFirst());
        model.addAttribute("orderdetail", filteredOrders);
//        for (OrderDetail order : allOrders) {
//            if (username.equals(order.getOrder().getCustomerName())) {
//                String productName = order.getProduct().getName();
//                if (numbers.stream().anyMatch(number -> number.getValuenumber().equals(productName))) {
//                    filteredOrdersa.add(order);
//                }
//            }
//        }
//        model.addAttribute("ordercorrec", filteredOrdersa);

        return "cart/orderdetail"; // Trả về view để hiển thị danh sách đơn hàng
    }

}
