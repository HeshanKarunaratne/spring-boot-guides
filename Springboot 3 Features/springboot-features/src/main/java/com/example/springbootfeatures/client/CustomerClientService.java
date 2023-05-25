package com.example.springbootfeatures.client;

import com.example.springbootfeatures.dto.Customer;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@HttpExchange("/api/customer")
public interface CustomerClientService {

    @GetExchange
    List<Customer> getAllCustomers();
}
