package com.example.swiggyservice.controller;

import com.example.swiggyservice.dto.OrderResponseDTO;
import com.example.swiggyservice.service.SwiggyAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/swiggy")
@Slf4j
public class SwiggyAppController {

    @Autowired
    private SwiggyAppService service;

    @GetMapping("/home")
    public String greetingMessage() {
        return service.greeting();
    }

    @GetMapping("/{orderId}")
    public OrderResponseDTO checkOrderStatus(@PathVariable String orderId, @RequestHeader("loggedInUser") String username) {
        log.info("LoggedInUser: {}", username);
        return service.checkOrderStatus(orderId);
    }
}