package com.example.pagingsortingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author Heshan Karunaratne
 */
@Data
@AllArgsConstructor
public class APIResponse<T> {
    private final int recordCount;
    private final T response;
}
