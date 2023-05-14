package com.example.springdataredis.repository;

import com.example.springdataredis.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Repository
@RequiredArgsConstructor
public class ProductDao {

    public static final String HASH_KEY = "Product";
    private final RedisTemplate redisTemplate;

    public Product save(Product product) {
        redisTemplate.opsForHash().put(HASH_KEY, product.getId(), product);
        return product;
    }

    public List<Product> findAll() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public Product findProductById(int id) {
        return (Product) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public String deleteProduct(int id) {
        redisTemplate.opsForHash().delete(HASH_KEY, id);
        return "Product removed";
    }
}
