package com.example.employeeservice.model;

/**
 * @author Heshan Karunaratne
 */
public record Employee(Long id, Long departmentId, String name, int age, String position) {
}
