package com.example.swiggyservice.service;

import com.example.swiggyservice.client.RestaurantServiceClient;
import com.example.swiggyservice.dto.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Heshan Karunaratne
 */
@Service
public class SwiggyAppService {

    @Autowired
    private RestaurantServiceClient restaurantServiceClient;

    public String greeting() {
        return "Welcome to Swiggy App Service";
    }

    public OrderResponseDTO checkOrderStatus(String orderId) {
        return restaurantServiceClient.fetchOrderStatus(orderId);
    }
}