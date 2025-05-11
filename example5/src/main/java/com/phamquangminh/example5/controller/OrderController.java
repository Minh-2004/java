package com.phamquangminh.example5.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.phamquangminh.example5.config.AppConstants;
import com.phamquangminh.example5.entity.Order;
import com.phamquangminh.example5.entity.PaymentStatus;
import com.phamquangminh.example5.payloads.OrderDTO;
import com.phamquangminh.example5.payloads.OrderResponse;
import com.phamquangminh.example5.repository.OrderRepo;
import com.phamquangminh.example5.service.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepo orderRepo;

    // 1. Đặt hàng
    @PostMapping("/public/users/{emailId}/carts/{cartId}/payments/{paymentMethod}/order")
    public ResponseEntity<OrderDTO> orderProducts(
            @PathVariable String emailId,
            @PathVariable Long cartId,
            @PathVariable String paymentMethod) {
        OrderDTO order = orderService.placeOrder(emailId, cartId, paymentMethod);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // 2. Lấy tất cả đơn hàng (ADMIN)
    @GetMapping("/admin/orders")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    // 3. Lấy danh sách đơn hàng của 1 user
    @GetMapping("/public/users/{emailId}/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String emailId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(emailId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 4. Lấy chi tiết đơn hàng theo user
    @GetMapping("/public/users/{emailId}/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrderByUser(
            @PathVariable String emailId,
            @PathVariable Long orderId) {
        OrderDTO order = orderService.getOrder(emailId, orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // 5. Admin cập nhật trạng thái đơn hàng
    @PutMapping("/admin/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
    public ResponseEntity<OrderDTO> updateOrderByUser(
            @PathVariable String emailId,
            @PathVariable Long orderId,
            @PathVariable String orderStatus) {
        OrderDTO order = orderService.updateOrder(emailId, orderId, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // 6. Kiểm tra trạng thái thanh toán (3 phút)
    @GetMapping("/public/orders/{orderId}/check-payment")
    public ResponseEntity<String> checkPaymentStatus(@PathVariable Long orderId) {
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }

        if (order.getPaymentStatus() == PaymentStatus.CONFIRMED) {
            return ResponseEntity.ok("SUCCESS");
        }

        Duration duration = Duration.between(order.getCreatedAt(), LocalDateTime.now());
        if (duration.toMinutes() >= 3 && order.getPaymentStatus() == PaymentStatus.PENDING_PAYMENT) {
            order.setPaymentStatus(PaymentStatus.REJECTED);
            orderRepo.save(order);
            return ResponseEntity.ok("REJECTED");
        }

        return ResponseEntity.ok("WAITING");
    }

    // 7. Xác nhận thanh toán
    @PostMapping("/public/orders/{orderId}/confirm-payment")
    public ResponseEntity<String> confirmPayment(@PathVariable Long orderId) {
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }

        if (order.getPaymentStatus() == PaymentStatus.PENDING_PAYMENT) {
            order.setPaymentStatus(PaymentStatus.CONFIRMED);
            orderRepo.save(order);
            return ResponseEntity.ok("Payment confirmed.");
        }

        return ResponseEntity.badRequest().body("Order is not in PENDING_PAYMENT status.");
    }
}
