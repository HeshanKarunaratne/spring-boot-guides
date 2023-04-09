package com.example.ServiceB.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/b")
public class ServiceController {

    @GetMapping
    public String serviceB() {
        return "Service B is called from Service A";
    }
}
