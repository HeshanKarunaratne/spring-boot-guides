package com.example.JavaFunctionals.Callbacks;

import java.util.function.BiFunction;

/**
 * @author Heshan Karunaratne
 */
public class Lambdas {
    public static void main(String[] args) {
        BiFunction<String, Integer, String> uppercaseName = (name, age) -> {
            if (name.isBlank()) throw new IllegalStateException("");
            System.out.println(age);
            return name.toUpperCase();
        };

        System.out.println(uppercaseName.apply("heshan", 12));
    }
}
