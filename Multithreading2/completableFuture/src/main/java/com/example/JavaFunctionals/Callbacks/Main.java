package com.example.JavaFunctionals.Callbacks;

import java.util.function.Consumer;

/**
 * @author Heshan Karunaratne
 */
public class Main {
    public static void main(String[] args) {
        hello("Heshan", null, value -> System.out.println("Last Name not provided for: " + value));

        hello2("Heshan", null, () -> System.out.println("Last Name not provided"));
    }

    static void hello(String firstName, String lastName, Consumer<String> callback) {
        System.out.println(firstName);
        if (lastName != null) {
            System.out.println(lastName);
        } else {
            callback.accept(firstName);
        }
    }

    static void hello2(String firstName, String lastName, Runnable callback) {
        System.out.println(firstName);
        if (lastName != null) {
            System.out.println(lastName);
        } else {
            callback.run();
        }
    }
}
