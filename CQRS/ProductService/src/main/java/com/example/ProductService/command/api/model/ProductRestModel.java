package com.example.ProductService.command.api.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Heshan Karunaratne
 */
@Data
@Builder
public class ProductRestModel {
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
