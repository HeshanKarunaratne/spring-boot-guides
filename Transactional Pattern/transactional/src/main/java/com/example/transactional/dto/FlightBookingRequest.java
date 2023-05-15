package com.example.transactional.dto;

import com.example.transactional.entity.PassengerInfo;
import com.example.transactional.entity.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Heshan Karunaratne
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightBookingRequest {

    private PassengerInfo passengerInfo;

    private PaymentInfo paymentInfo;
}