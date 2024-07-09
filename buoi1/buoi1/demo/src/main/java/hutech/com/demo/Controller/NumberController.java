package hutech.com.demo.Controller;

import hutech.com.demo.model.Number;
import hutech.com.demo.model.OrderDetail;
import hutech.com.demo.service.NumberService;
import hutech.com.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller

@RequiredArgsConstructor

public class NumberController {
    @Autowired
    private final NumberService numberService;
    @Autowired
    private OrderService orderService;
    @GetMapping("/numbers/add")
    public String showAddForm(Model model) {
        model.addAttribute("number", new Number());
        return "/numbers/add-number";

    }
    @PostMapping("/numbers/add")
    public String addCategory(@Valid Number number, BindingResult result) {
        if (result.hasErrors()) {
            return "/numbers/add-number";
        }

        numberService.addNumber(number);
        return "redirect:/numbers";
    }
    @GetMapping("/numbers")
    public String listCategories(Model model) {
        List<Number> numbers = numberService.getAllNumbers();
        model.addAttribute("numbers", numbers);
        return "/numbers/numbers-list";
    }
    @GetMapping("/")
    public String listCategoriess(Model model) {
        List<Number> numbers = numberService.getAllNumbers();
        if (!numbers.isEmpty()) {
            Number lastNumber = numbers.get(numbers.size() - 1);
            model.addAttribute("numbers", lastNumber);
        }
        return "/home";
    }
    // GET request to show category edit form
    @GetMapping("/numbers/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Number number = numberService.getNumberById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:"
                        + id));
        model.addAttribute("number", number);
        return "/numbers/update-numbers";
    }
    @PostMapping("/numbers/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, @Valid Number number,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            number.setId(id);
            return "/numbers/update-number";
        }
        numberService.updateNumber(number);
        model.addAttribute("numbers", numberService.getAllNumbers());
        return "redirect:/numbers";
    }
    @GetMapping("/numbers/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
        Number number = numberService.getNumberById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:"
                        + id));
        numberService.deleteNumberById(id);
        model.addAttribute("numbers", numberService.getAllNumbers());
        return "redirect:/numbers";
    }
    @GetMapping("/numbers/orderdetail/{id}")
    public String getAllOrders(@PathVariable("id") String id,Model model) {
        List<OrderDetail> allOrders = orderService.getAllOrderDetail();
        List<OrderDetail> filteredOrders = new ArrayList<>();
        String dateString = id.split(" ")[1];
        String numbercorrec = id.split(" ")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        LocalDateTime parsedDate = LocalDateTime.parse(dateString, formatter);
        for (OrderDetail order : allOrders) {
            if (numbercorrec.equals(order.getProduct().getName()) && parsedDate.isAfter(order.getCreatedAt())) {
                filteredOrders.add(order);
            }
        }

        model.addAttribute("orderdetail", filteredOrders);
        return "/numbers/orderdetail"; // Trả về view để hiển thị danh sách đơn hàng
    }
}
