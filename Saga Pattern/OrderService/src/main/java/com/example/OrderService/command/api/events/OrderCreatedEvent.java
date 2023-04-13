package com.example.OrderService.command.api.events;

import lombok.Data;

/**
 * @author Heshan Karunaratne
 */
@Data
public class OrderCreatedEvent {
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;
}
