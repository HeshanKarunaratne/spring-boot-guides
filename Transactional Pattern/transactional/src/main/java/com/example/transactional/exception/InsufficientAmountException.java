package com.example.transactional.exception;

/**
 * @author Heshan Karunaratne
 */
public class InsufficientAmountException extends RuntimeException {

    public InsufficientAmountException(String msg) {
        super(msg);
    }
}