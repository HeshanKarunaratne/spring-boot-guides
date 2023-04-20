package com.example.ShipmentService.command.api.events;

import com.example.CommonService.events.OrderShippedEvent;
import com.example.ShipmentService.command.api.data.Shipment;
import com.example.ShipmentService.command.api.data.ShipmentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author Heshan Karunaratne
 */
@Component
public class ShipmentEventsHandler {

    private final ShipmentRepository shipmentRepository;

    public ShipmentEventsHandler(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(event, shipment);

        shipmentRepository.save(shipment);
    }
}
