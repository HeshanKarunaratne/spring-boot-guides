package com.example.springbatch1.repo;

import com.example.springbatch1.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
