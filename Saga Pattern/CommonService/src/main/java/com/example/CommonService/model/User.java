package com.example.CommonService.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Heshan Karunaratne
 */
@Data
@Builder
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private CardDetails cardDetails;
}
