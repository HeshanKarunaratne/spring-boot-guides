package com.example.JavaFunctionals.CombinatorPattern;

import java.time.LocalDate;

import static com.example.JavaFunctionals.CombinatorPattern.CustomerRegistrationValidator.*;

/**
 * @author Heshan Karunaratne
 */
public class Main {
    public static void main(String[] args) {

        Customer customer = new Customer(
                "Heshan",
                "heshan@gmail.com",
                "+052451331",
                LocalDate.of(2015, 1, 1));

//        Using Combinator Pattern
        ValidationResult result = isEmailValid()
                .and(isPhoneNumberValid())
                .and(isAnAdult())
                .apply(customer);

        System.out.println(result);
        if (result != ValidationResult.SUCCESS) {
            throw new IllegalStateException(result.name());
        }
    }
}
