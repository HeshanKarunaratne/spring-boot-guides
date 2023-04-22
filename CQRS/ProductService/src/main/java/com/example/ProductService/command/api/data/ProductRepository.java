package com.example.ProductService.command.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface ProductRepository extends JpaRepository<Product, String> {
}
