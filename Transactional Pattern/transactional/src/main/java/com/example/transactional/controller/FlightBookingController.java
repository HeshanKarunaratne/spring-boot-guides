package com.example.transactional.controller;

import com.example.transactional.dto.FlightBookingAcknowledgement;
import com.example.transactional.dto.FlightBookingRequest;
import com.example.transactional.service.FlightBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/api/flight-book")
@RequiredArgsConstructor
public class FlightBookingController {
    private final FlightBookingService flightBookingService;

    @PostMapping
    public FlightBookingAcknowledgement bookFlightTicket(@RequestBody FlightBookingRequest request) {
        return flightBookingService.bookFlightTicket(request);
    }
}
