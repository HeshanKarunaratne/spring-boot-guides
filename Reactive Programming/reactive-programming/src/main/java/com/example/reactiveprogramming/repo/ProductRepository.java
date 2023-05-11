package com.example.reactiveprogramming.repo;

import com.example.reactiveprogramming.dto.ProductDto;
import com.example.reactiveprogramming.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @author Heshan Karunaratne
 */
@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Flux<ProductDto> findByPriceBetween(Range<Double> priceRange);
}
