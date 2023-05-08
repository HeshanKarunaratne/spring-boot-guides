package com.example.JavaFunctionals.CombinatorPattern;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Function;

import static com.example.JavaFunctionals.CombinatorPattern.CustomerRegistrationValidator.ValidationResult;
import static com.example.JavaFunctionals.CombinatorPattern.CustomerRegistrationValidator.ValidationResult.*;

/**
 * @author Heshan Karunaratne
 */
public interface CustomerRegistrationValidator
        extends Function<Customer, ValidationResult> {

    enum ValidationResult {
        SUCCESS,
        PHONE_NUMBER_NOT_VALID,
        EMAIL_NOT_VALID,
        IS_NOT_AN_ADULT
    }

    static CustomerRegistrationValidator isEmailValid() {
        return customer -> customer.getEmail().contains("@") ? SUCCESS : EMAIL_NOT_VALID;
    }

    static CustomerRegistrationValidator isPhoneNumberValid() {
        return customer -> customer.getPhoneNumber().startsWith("+0") ? SUCCESS : PHONE_NUMBER_NOT_VALID;
    }

    static CustomerRegistrationValidator isAnAdult() {
        return customer -> Period.between(customer.getDob(), LocalDate.now()).getYears() > 16 ? SUCCESS : IS_NOT_AN_ADULT;
    }

    default CustomerRegistrationValidator and(CustomerRegistrationValidator other) {
        return customer -> {
            ValidationResult result = this.apply(customer);
            return result.equals(SUCCESS) ? other.apply(customer) : result;
        };
    }
}
