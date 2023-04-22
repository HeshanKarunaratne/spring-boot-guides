package com.example.CommonService.events;

import lombok.Data;

/**
 * @author Heshan Karunaratne
 */
@Data
public class PaymentCancelledEvent {
    private String paymentId;
    private String orderId;
    private String paymentStatus;
}
