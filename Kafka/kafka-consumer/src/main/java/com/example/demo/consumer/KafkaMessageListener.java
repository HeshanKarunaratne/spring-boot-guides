package com.example.demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author Heshan Karunaratne
 */
@Service
@Slf4j
public class KafkaMessageListener {

    @KafkaListener(topics = "demo2", groupId = "test")
    public void consumer(String message) {
        log.info("consumed message: " + message);
    }
}
