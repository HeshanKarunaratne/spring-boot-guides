package com.example.OrderService.command.api.saga;

import com.example.OrderService.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

/**
 * @author Heshan Karunaratne
 */
@Saga
@Slf4j
public class OrderProcessingSage {

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Sage for Order Id: {}", event.getOrderId());
    }
}
