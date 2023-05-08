package com.example.JavaFunctionals.Optionals;

import java.util.Optional;

/**
 * @author Heshan Karunaratne
 */
public class Main {
    public static void main(String[] args) {
        Object o = Optional.ofNullable(null)
                .orElseThrow(() -> new IllegalStateException("Execption"));

        System.out.println(o);
    }
}
