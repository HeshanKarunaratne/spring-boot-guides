package com.example.CommonService.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author Heshan Karunaratne
 */
@Data
@Builder
public class ShipOrderCommand {

    @TargetAggregateIdentifier
    private String shipmentId;
    private String orderId;
}
