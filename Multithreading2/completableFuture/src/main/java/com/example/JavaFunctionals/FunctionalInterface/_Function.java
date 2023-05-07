package com.example.JavaFunctionals.FunctionalInterface;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Heshan Karunaratne
 */
public class _Function {
    public static void main(String[] args) {
        int increment = incrementByOne(0);
        System.out.println(increment);

        Function<Integer, Integer> addOneAndThenMultiplyByTen = incrementByOneFunction.andThen(multiplyByTenFunction);
        Integer apply = addOneAndThenMultiplyByTen.apply(0);
        System.out.println(apply);

        int first = incrementByOneAndMultiplyByTen(1, 10);

        System.out.println("first: " + first);
        Integer second = incrementByOneAndMultiplyByTenFunction.apply(1, 10);
        System.out.println("second: " + second);
    }

    static Function<Integer, Integer> incrementByOneFunction = number -> number + 1;

    static Function<Integer, Integer> multiplyByTenFunction = number -> number * 10;

    static int incrementByOne(int num) {
        return num + 1;
    }

    static int incrementByOneAndMultiplyByTen(int num, int num2) {
        return (num + 1) * num2;
    }

    static BiFunction<Integer, Integer, Integer> incrementByOneAndMultiplyByTenFunction = (num, num2) -> (num + 1) * num2;
}
