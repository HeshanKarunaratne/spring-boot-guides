package com.example.demo.controller;

import com.example.demo.dto.Customer;
import com.example.demo.service.KafkaMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/api/v1/producer")
public class EventController {

    @Autowired
    private KafkaMessagePublisher publisher;

    @GetMapping("/publish/{message}")
    public ResponseEntity<?> publishMessage(@PathVariable("message") String message) {
        try {
            publisher.sendMessageToTopic(message);
            return ResponseEntity.ok("Message published Successfully...");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/publish")
    public void sendEvents(@RequestBody Customer customer) {
        publisher.sendEventsToTopic(customer);
    }
}
