package com.example.exceptionhandlingservice1.exception;

/**
 * @author Heshan Karunaratne
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
