package com.example.springdataredis;

import com.example.springdataredis.entity.Product;
import com.example.springdataredis.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class SpringDataRedisApplication {

    private final ProductDao dao;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataRedisApplication.class, args);
    }

    @PostMapping
    public Product save(@RequestBody Product product) {
        return dao.save(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return dao.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return dao.findProductById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) {
        return dao.deleteProduct(id);
    }
}
