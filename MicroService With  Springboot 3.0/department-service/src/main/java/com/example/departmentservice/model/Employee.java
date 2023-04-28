package com.example.departmentservice.model;

/**
 * @author Heshan Karunaratne
 */
public record Employee(Long id, Long departmentId, String name, int age, String position) {
}
