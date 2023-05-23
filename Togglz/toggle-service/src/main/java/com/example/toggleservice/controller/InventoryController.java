package com.example.toggleservice.controller;

import com.example.toggleservice.dto.Product;
import com.example.toggleservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.util.NamedFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    private final FeatureManager manager;

    public static final Feature DISCOUNT_APPLIED = new NamedFeature("DISCOUNT_APPLIED");

    @GetMapping
    public List<Product> showAvailableProducts() {
        if (manager.isActive(DISCOUNT_APPLIED))
            return applyDiscount(inventoryService.getAllProducts());

        return inventoryService.getAllProducts();
    }

    private List<Product> applyDiscount(List<Product> availableProducts) {
        List<Product> orderListAfterDiscount = new ArrayList<>();
        inventoryService.getAllProducts().forEach(
                order -> {
                    order.setPrice(order.getPrice() - (order.getPrice() * 5 / 100));
                    orderListAfterDiscount.add(order);
                }
        );
        return orderListAfterDiscount;
    }
}
