package com.example.OrderService.command.api.saga;

import com.example.CommonService.commands.CompleteOrderCommand;
import com.example.CommonService.commands.ShipOrderCommand;
import com.example.CommonService.commands.ValidatePaymentCommand;
import com.example.CommonService.events.OrderCompletedEvent;
import com.example.CommonService.events.OrderShippedEvent;
import com.example.CommonService.events.PaymentProcessedEvent;
import com.example.CommonService.model.User;
import com.example.CommonService.queries.GetUserPaymentDetailsQuery;
import com.example.OrderService.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;

import java.util.UUID;

/**
 * @author Heshan Karunaratne
 */
@Saga
@Slf4j
public class OrderProcessingSage {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public OrderProcessingSage(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Sage for Order Id: {}", event.getOrderId());

        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(event.getUserId());
        User user = null;
        try {
            user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception e) {
            log.error(e.getMessage());
            //Start the Compensating transaction

        }

        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .cardDetails(user.getCardDetails())
                .orderId(event.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .build();

        commandGateway.sendAndWait(validatePaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent in Sage for Order Id: {}", event.getOrderId());
        try {
            ShipOrderCommand shipOrderCommand = ShipOrderCommand.builder()
                    .orderId(event.getOrderId())
                    .shipmentId(UUID.randomUUID().toString())
                    .build();

            commandGateway.send(shipOrderCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            //Start the compensating transaction
        }

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent event) {
        log.info("OrderShippedEvent in Sage for Order Id: {}", event.getOrderId());
        CompleteOrderCommand completeOrderCommand = CompleteOrderCommand.builder()
                .orderId(event.getOrderId())
                .orderStatus("APPROVED")
                .build();
        commandGateway.send(completeOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCompletedEvent event){
        log.info("OrderCompletedEvent in Sage for Order Id: {}", event.getOrderId());
    }
}
