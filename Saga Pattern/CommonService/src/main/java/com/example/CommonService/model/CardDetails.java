package com.example.CommonService.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Heshan Karunaratne
 */
@Data
@Builder
public class CardDetails {
    private String name;
    private String cardNumber;
    private Integer validUntilMonth;
    private Integer validUntilYear;
    private Integer cvv;
}
