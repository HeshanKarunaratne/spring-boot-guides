package com.example.JavaFunctionals.FunctionalInterface;

import java.util.function.Predicate;

/**
 * @author Heshan Karunaratne
 */
public class _Predicate {
    public static void main(String[] args) {
        System.out.println(isPhoneNumberValid("07000000000"));
        System.out.println(isPhoneNumberValid("09000000000"));
        System.out.println(isPhoneNumberValid("0700000000"));

        System.out.println("*****");
        System.out.println(isPhoneNumberValidPredicate.test("07000000000"));
        System.out.println(isPhoneNumberValidPredicate.test("09000000000"));
        System.out.println(isPhoneNumberValidPredicate.test("0700000000"));
    }

    static boolean isPhoneNumberValid(String number) {
        return number.startsWith("07") && number.length() == 11;
    }

    static Predicate<String> isPhoneNumberValidPredicate = number -> number.startsWith("07") && number.length() == 11;
}
