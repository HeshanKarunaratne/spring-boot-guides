package com.example.ShipmentService.command.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Heshan Karunaratne
 */
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}
