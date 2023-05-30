package com.example.restaurentservice.service;

import com.example.restaurentservice.dao.RestaurantOrderDAO;
import com.example.restaurentservice.dto.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Heshan Karunaratne
 */
@Service
public class RestaurantService {
    @Autowired
    private RestaurantOrderDAO orderDAO;

    public String greeting() {
        return "Welcome to Swiggy Restaurant service";
    }

    public OrderResponseDTO getOrder(String orderId) {
        return orderDAO.getOrders(orderId);
    }
}