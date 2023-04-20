package com.example.PaymentService.command.api.events;

import com.example.CommonService.events.PaymentProcessedEvent;
import com.example.PaymentService.command.api.data.Payment;
import com.example.PaymentService.command.api.data.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Heshan Karunaratne
 */
@Component
public class PaymentsEventsHandler {

    private final PaymentRepository paymentRepository;

    public PaymentsEventsHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .timestamp(new Date())
                .build();

        paymentRepository.save(payment);
    }
}
