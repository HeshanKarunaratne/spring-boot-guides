package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Multiple values as PathVariables -> http://localhost:8082/api/inventory/iphone13,iphone13Red
    // Multiple values as RequestParams -> http://localhost:8082/api/inventory?skuCode=iphone13&skuCode=iphone13Red
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) throws InterruptedException {
        return inventoryService.isInStock(skuCode);
    }
}
