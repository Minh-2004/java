package com.phamquangminh.example5.controller;

import com.phamquangminh.example5.payloads.PaymentDTO;
import com.phamquangminh.example5.payloads.PaymentRequestDTO;
import com.phamquangminh.example5.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/simulate")
    public ResponseEntity<PaymentDTO> simulatePayment(@RequestBody PaymentRequestDTO request) {
        PaymentDTO result = paymentService.simulatePayment(request);
        return ResponseEntity.ok(result);
    }
}