package com.example.reactiveprogramming.controller;

import com.example.reactiveprogramming.dto.Customer;
import com.example.reactiveprogramming.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return service.loadAllCustomers();
    }

    @GetMapping(value = "/reactive", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> getAllCustomersReactive() {
        return service.loadAllCustomersReactive();
    }
}
