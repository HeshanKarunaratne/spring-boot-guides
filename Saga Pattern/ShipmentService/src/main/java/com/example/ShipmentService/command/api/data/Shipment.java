package com.example.ShipmentService.command.api.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Heshan Karunaratne
 */
@Data
@Entity
public class Shipment {
    @Id
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;
}
