package com.example.OrderService.command.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface OrderRepository extends JpaRepository<Order, String> {

}
