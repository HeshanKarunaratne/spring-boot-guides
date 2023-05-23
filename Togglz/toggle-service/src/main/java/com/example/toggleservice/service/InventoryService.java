package com.example.toggleservice.service;

import com.example.toggleservice.dto.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Heshan Karunaratne
 */
@Service
public class InventoryService {

    public List<Product> getAllProducts() {
        return Stream.of(
                new Product(1, "Mobile", 50000),
                new Product(2, "Lap", 2000),
                new Product(3, "TV", 14999),
                new Product(4, "glass", 999)
        ).collect(Collectors.toList());
    }
}
