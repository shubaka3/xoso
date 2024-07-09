package hutech.com.demo.service;

import hutech.com.demo.model.CartItem;
import hutech.com.demo.model.Order;
import hutech.com.demo.model.OrderDetail;
import hutech.com.demo.repository.OrderDetailRepository;
import hutech.com.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional

public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService; // Assuming you have a CartService

    @Transactional
    public Order createOrder(String customerName, String address, String phone, String email, String note, String payment, List<CartItem> cartItems) {
        Double totalPrice = 0.0;
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setAddress(address);
        order.setPhone(phone);
        order.setEmail(email);
        order.setNote(note);
        order.setPaymentMethod(payment);

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());

            // Convert double price to BigDecimal
            Double itemPrice = item.getProduct().getPrice();
            Double itemTotalPrice = itemPrice * item.getQuantity();
            detail.setPrice(itemTotalPrice);

            // Add to total price
            totalPrice += itemTotalPrice;


            orderDetailRepository.save(detail);
        }

        // Set total price for the order
        order.setTotalPrice(totalPrice);
        order = orderRepository.save(order);

        cartService.clearCart();
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
    }
    public List<OrderDetail> getAllOrderDetail() {
        return orderDetailRepository.findAll();
    }
}
