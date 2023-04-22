package com.example.CommonService.events;

import lombok.Data;

/**
 * @author Heshan Karunaratne
 */
@Data
public class OrderCancelledEvent {
    private String orderId;
    private String orderStatus;
}
