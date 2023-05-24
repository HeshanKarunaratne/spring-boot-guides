package com.example.springbootfeatures.dto;

import org.springframework.http.HttpStatus;

/**
 * @author Heshan Karunaratne
 */
public class Error {
    private HttpStatus status;
    private int errorCode;
    private String errorMessage;
}
