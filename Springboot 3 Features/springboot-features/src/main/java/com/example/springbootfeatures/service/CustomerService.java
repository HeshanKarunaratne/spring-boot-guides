package com.example.springbootfeatures.service;

import com.example.springbootfeatures.dto.Customer;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Service
public class CustomerService {
    private List<Customer> customerList = new ArrayList<>();
    private final ObservationRegistry registry;

    @Autowired
    public CustomerService(ObservationRegistry registry) {
        this.registry = registry;
    }

    public Customer addCustomer(Customer customer) {
        customerList.add(customer);
        return Observation.createNotStarted("addCustomer", registry)
                .observe(() -> customer);
    }

    public List<Customer> getCustomers() {
        return Observation.createNotStarted("getCustomers", registry)
                .observe(() -> customerList);
    }

    public Customer getCustomer(int id) {
        return Observation.createNotStarted("getCustomer", registry)
                .observe(() -> customerList
                        .stream()
                        .filter(customer -> customer.id() == id)
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("Customer not found: " + id)));
    }
}
