package com.example.swiggyservice.controller;

import com.example.swiggyservice.dto.OrderResponseDTO;
import com.example.swiggyservice.service.SwiggyAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public OrderResponseDTO checkOrderStatus(@PathVariable String orderId) {
        log.info("inside swiggy controller");
        return service.checkOrderStatus(orderId);
    }
}