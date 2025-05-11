package com.phamquangminh.example5.service.impl;

import com.phamquangminh.example5.entity.Payment;
import com.phamquangminh.example5.entity.Order;
import com.phamquangminh.example5.repository.OrderRepo;
import com.phamquangminh.example5.payloads.PaymentDTO;
import com.phamquangminh.example5.payloads.PaymentRequestDTO;
import com.phamquangminh.example5.repository.PaymentRepository;
import com.phamquangminh.example5.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final OrderRepo orderRepo;

    @Override
    public PaymentDTO simulatePayment(PaymentRequestDTO request) {
        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new NoSuchElementException("Order không tồn tại"));

        // Kiểm tra đơn hàng đã thanh toán chưa
        if (order.getPayment() != null) {
            throw new IllegalStateException("Đơn hàng đã được thanh toán");
        }

        // Tạo bản ghi thanh toán giả lập
        Payment payment = new Payment();
        payment.setPaymentMethod(request.getPaymentMethod());
        Payment savedPayment = paymentRepo.save(payment);

        // Gán payment vào order
        order.setPayment(savedPayment);
        orderRepo.save(order);

        return new PaymentDTO(savedPayment.getPaymentId(), savedPayment.getPaymentMethod());
    }
}