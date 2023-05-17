package com.example.springbatch1.config;

import com.example.springbatch1.entity.Customer;
import com.example.springbatch1.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author Heshan Karunaratne
 */
@Component
public class CustomerWriter implements ItemWriter<Customer> {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void write(List<? extends Customer> items) throws Exception {
        System.out.println("Thread Name: " + Thread.currentThread().getName());
        customerRepository.saveAll(items);
    }
}
