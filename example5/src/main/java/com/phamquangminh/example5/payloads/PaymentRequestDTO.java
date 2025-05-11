package com.phamquangminh.example5.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private Long orderId;
    private String paymentMethod; // e.g., "VNPAY", "MOMO", "CASH"
}
