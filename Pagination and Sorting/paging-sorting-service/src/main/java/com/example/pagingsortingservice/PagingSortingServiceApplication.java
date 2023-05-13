package com.example.pagingsortingservice;

import com.example.pagingsortingservice.dto.APIResponse;
import com.example.pagingsortingservice.entity.Product;
import com.example.pagingsortingservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/sort/{field}")
    public APIResponse<List<Product>> getProductsWithSort(@PathVariable String field) {
        List<Product> allProducts = productService.findProductsWithSorting(field);
        return new APIResponse<>(allProducts.size(), allProducts);
    }

    @GetMapping("/paging/{offset}/{pageSize}")
    public APIResponse<Page<Product>> getProductsWithPaging(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Product> allProducts = productService.findProductsWithPaging(offset, pageSize);
        return new APIResponse<>(allProducts.getSize(), allProducts);
    }

    @GetMapping("/sort/{field}/paging/{offset}/{pageSize}")
    public APIResponse<Page<Product>> getProductsWithPagingAndSorting(@PathVariable int offset, @PathVariable int pageSize, @PathVariable String field) {
        Page<Product> allProducts = productService.findProductsWithPagingAndSorting(offset, pageSize, field);
        return new APIResponse<>(allProducts.getSize(), allProducts);
    }
}
