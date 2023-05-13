package com.example.pagingsortingservice.service;

import com.example.pagingsortingservice.entity.Product;
import com.example.pagingsortingservice.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

//    @PostConstruct
//    public void initDB() {
//        List<Product> products = IntStream.rangeClosed(1, 200)
//                .mapToObj(i -> new Product(
//                        i,
//                        "Product" + i,
//                        new Random().nextInt(100),
//                        new Random().nextInt(50000)))
//                .collect(Collectors.toList());
//        repository.saveAll(products);
//    }

    public List<Product> findAllProducts() {
        return repository.findAll();
    }
}
