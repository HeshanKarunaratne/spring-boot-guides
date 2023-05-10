package com.example.reactiveprogramming.service;

import com.example.reactiveprogramming.dao.CustomerDao;
import com.example.reactiveprogramming.dto.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerDao customerDao;

    public List<Customer> loadAllCustomers() {
        long start = System.currentTimeMillis();
        List<Customer> customers = customerDao.getCustomers();
        long end = System.currentTimeMillis();
        System.out.println("Execution Time: " + (end - start) + "ms");
        return customers;
    }

    public Flux<Customer> loadAllCustomersReactive() {
        long start = System.currentTimeMillis();
        Flux<Customer> customers = customerDao.getCustomersReactive();
        long end = System.currentTimeMillis();
        System.out.println("Execution Time: " + (end - start) + "ms");
        return customers;
    }
}
