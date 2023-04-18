package com.example.CommonService.commands;

import com.example.CommonService.model.CardDetails;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author Heshan Karunaratne
 */
@Data
@Builder
public class ValidatePaymentCommand {

    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private CardDetails cardDetails;
}
