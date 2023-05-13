package com.example.pagingsortingservice.repo;

import com.example.pagingsortingservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
