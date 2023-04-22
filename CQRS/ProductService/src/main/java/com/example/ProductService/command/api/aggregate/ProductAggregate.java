package com.example.ProductService.command.api.aggregate;

import com.example.ProductService.command.api.commands.CreateProductCommand;
import com.example.ProductService.command.api.events.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

/**
 * @author Heshan Karunaratne
 */
@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        // Perform validations
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(createProductCommand.getProductId())
                .name(createProductCommand.getName())
                .price(createProductCommand.getPrice())
                .quantity(createProductCommand.getQuantity())
                .build();
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        this.name = event.getName();
        this.price = event.getPrice();
        this.productId = event.getProductId();
        this.quantity = event.getQuantity();
    }
}
