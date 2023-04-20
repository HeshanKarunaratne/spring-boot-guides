package com.example.CommonService.events;

import lombok.Builder;
import lombok.Data;

/**
 * @author Heshan Karunaratne
 */
@Data
@Builder
public class OrderCompletedEvent {
    private String orderId;
    private String orderStatus;
}
