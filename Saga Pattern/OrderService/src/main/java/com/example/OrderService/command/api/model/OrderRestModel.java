package com.example.OrderService.command.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Heshan Karunaratne
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRestModel {
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;

}
