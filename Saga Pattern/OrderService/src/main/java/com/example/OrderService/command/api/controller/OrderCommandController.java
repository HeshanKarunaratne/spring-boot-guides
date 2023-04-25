package com.example.OrderService.command.api.controller;

import com.example.OrderService.command.api.command.CreateOrderCommand;
import com.example.OrderService.command.api.model.OrderRestModel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderCommandController {
    private transient CommandGateway commandGateway;

    public OrderCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createOrder(@RequestBody OrderRestModel orderRestModel) {
        String orderId = UUID.randomUUID().toString();
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(orderId)
                .addressId(orderRestModel.getAddressId())
                .productId(orderRestModel.getProductId())
                .quantity(orderRestModel.getQuantity())
                .orderStatus("CREATED")
                .userId(orderRestModel.getUserId())
                .build();
        log.info("Order Created for {}", orderId);
        return commandGateway.sendAndWait(createOrderCommand);
    }
}
