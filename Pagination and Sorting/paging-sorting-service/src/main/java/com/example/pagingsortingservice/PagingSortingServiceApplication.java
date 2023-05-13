package com.example.pagingsortingservice;

import com.example.pagingsortingservice.dto.APIResponse;
import com.example.pagingsortingservice.entity.Product;
import com.example.pagingsortingservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class PagingSortingServiceApplication {
    private final ProductService productService;

    public static void main(String[] args) {
        SpringApplication.run(PagingSortingServiceApplication.class, args);
    }

    @GetMapping
    public APIResponse<List<Product>> getProducts() {
        List<Product> allProducts = productService.findAllProducts();
        return new APIResponse<>(allProducts.size(), allProducts);
    }
}
