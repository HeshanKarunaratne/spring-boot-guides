package com.example.PaymentService.command.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface PaymentRepository extends JpaRepository<Payment, String> {
}
