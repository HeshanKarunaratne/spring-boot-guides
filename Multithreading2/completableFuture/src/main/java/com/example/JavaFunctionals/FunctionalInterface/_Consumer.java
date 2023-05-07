package com.example.JavaFunctionals.FunctionalInterface;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Heshan Karunaratne
 */
public class _Consumer {
    public static void main(String[] args) {
        greetCustomer(new Customer("Heshan", "1234567890"));
        greetCustomerConsumer.accept(new Customer("Heshan2", "1234567890"));
        greetCustomerConsumerV2.accept(new Customer("Heshan3", "1234567890"), true);
        greetCustomerConsumerV2.accept(new Customer("Heshan4", "1234567890"), false);
    }

    static BiConsumer<Customer, Boolean> greetCustomerConsumerV2 = (customer, showPhoneNumber) ->
            System.out.println("Hello " + customer.customerName
                    + ", thanks for registering phone number "
                    + (showPhoneNumber ? customer.customerPhoneNumber : "**********"));

    static Consumer<Customer> greetCustomerConsumer = customer -> System.out.println("Hello " + customer.customerName
            + ", thanks for registering phone number "
            + customer.customerPhoneNumber);

    static void greetCustomer(Customer customer) {
        System.out.println("Hello " + customer.customerName
                + ", thanks for registering phone number "
                + customer.customerPhoneNumber);
    }

    @Data
    @AllArgsConstructor
    static class Customer {
        private final String customerName;
        private final String customerPhoneNumber;
    }
}