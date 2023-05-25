package com.example.springbootfeatures.controller;

import com.example.springbootfeatures.client.CustomerClientService;
import com.example.springbootfeatures.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/api/client")
public class CustomerClientController {

    @Autowired
    private CustomerClientService clientService;

    @GetMapping
    public List<Customer> fetchCustomers() {
        return clientService.getAllCustomers();
    }
}
