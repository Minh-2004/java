package com.phamquangminh.example5.service;

import com.phamquangminh.example5.payloads.PaymentDTO;
import com.phamquangminh.example5.payloads.PaymentRequestDTO;

public interface PaymentService {
    PaymentDTO simulatePayment(PaymentRequestDTO paymentRequest);
}
