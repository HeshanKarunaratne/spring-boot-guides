package com.example.pagingsortingservice.service;

import com.example.pagingsortingservice.entity.Product;
import com.example.pagingsortingservice.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public List<Product> findProductsWithSorting(String field) {
        return repository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public Page<Product> findProductsWithPaging(int offset, int pageSize) {
        return repository.findAll(PageRequest.of(offset, pageSize));
    }

    public Page<Product> findProductsWithPagingAndSorting(int offset, int pageSize, String field) {
        return repository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.ASC, field)));
    }

    public Page<Product> getProductsWithPagingAndSortingV2(String field, String direction, int offset, int pageSize) {
        Sort.Direction dir = direction.isBlank() ? Sort.Direction.ASC : Sort.Direction.ASC.toString().equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return repository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(dir, field)));
    }
}
