package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author Heshan Karunaratne
 */
@Service
@Slf4j
public class KafkaMessagePublisher {
    @Autowired
    private KafkaTemplate<String, Object> template;

    public void sendMessageToTopic(String message) {
        final CompletableFuture<SendResult<String, Object>> future = template.send("demo2", message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("SENT_MESSAGE: " + message);
            } else {
                log.error("UNABLE_TO_SEND_MESSAGE: " + ex.getMessage());
            }
        });
    }
}
