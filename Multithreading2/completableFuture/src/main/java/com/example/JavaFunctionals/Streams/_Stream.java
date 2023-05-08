package com.example.JavaFunctionals.Streams;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.JavaFunctionals.Streams._Stream.Gender.FEMALE;
import static com.example.JavaFunctionals.Streams._Stream.Gender.MALE;

/**
 * @author Heshan Karunaratne
 */
public class _Stream {
    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("John", MALE),
                new Person("Maria", FEMALE),
                new Person("Aisha", FEMALE),
                new Person("Alex", MALE),
                new Person("Alice", FEMALE)
        );

        people.stream()
                .map(person -> person.gender)
                .collect(Collectors.toSet())
                .forEach(System.out::println);
    }

    @Data
    @AllArgsConstructor
    static class Person {
        private final String name;
        private final Gender gender;

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", gender=" + gender +
                    '}';
        }
    }

    enum Gender {
        MALE, FEMALE
    }
}
