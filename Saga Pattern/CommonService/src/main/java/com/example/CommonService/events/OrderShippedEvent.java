package com.example.CommonService.events;

import lombok.Builder;
import lombok.Data;

/**
 * @author Heshan Karunaratne
 */
@Data
@Builder
public class OrderShippedEvent {
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;
}
