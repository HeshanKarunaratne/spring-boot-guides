package com.example.JavaFunctionals.CombinatorPattern;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Heshan Karunaratne
 */
@Data
@AllArgsConstructor
public class Customer {
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final LocalDate dob;
}
