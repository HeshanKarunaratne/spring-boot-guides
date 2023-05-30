package com.example.reactiveprogramming.controller;

import com.example.reactiveprogramming.dto.ProductDto;
import com.example.reactiveprogramming.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @GetMapping("/range")
    public Flux<ProductDto> getProductsInPriceRange(@RequestParam("min") double min, @RequestParam("max") double max) {
        return productService.getProductsInRange(min, max);
    }

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDto) {
        return productService.saveProduct(productDto);
    }

    @PutMapping("/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDto, @PathVariable String id) {
        return productService.updateProduct(productDto, id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

}
