package com.example.reactiveprogramming.util;

import com.example.reactiveprogramming.dto.ProductDto;
import com.example.reactiveprogramming.entity.Product;
import org.springframework.beans.BeanUtils;

/**
 * @author Heshan Karunaratne
 */
public class AppUtils {

    public static ProductDto entityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    public static Product dtoToEntity(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }
}
