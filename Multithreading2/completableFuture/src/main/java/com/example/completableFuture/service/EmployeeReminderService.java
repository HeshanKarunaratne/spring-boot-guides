package com.example.completableFuture.service;

import com.example.completableFuture.database.EmployeeDatabase;
import com.example.completableFuture.dto.Employee;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Heshan Karunaratne
 */
public class EmployeeReminderService {

    public void sendReminderToEmployee() {
        CompletableFuture.supplyAsync(() -> {

            System.out.println("FetchEmployee: " + Thread.currentThread().getName());
            return EmployeeDatabase.fetchEmployees();

        }).thenApply(employees -> {

            System.out.println("FilterNewEmployee: " + Thread.currentThread().getName());
            return employees
                    .stream()
                    .filter(employee -> "TRUE".equalsIgnoreCase(employee.getNewJoiner()))
                    .collect(Collectors.toList());

        }).thenApply(employees -> {

            System.out.println("FilterTrainingNotComplete: " + Thread.currentThread().getName());
            return employees
                    .stream()
                    .filter(employee -> "TRUE".equalsIgnoreCase(employee.getLearningPending()))
                    .collect(Collectors.toList());

        }).thenApply(employees -> {

            System.out.println("GetEmails: " + Thread.currentThread().getName());
            return employees.stream().map(Employee::getEmail)
                    .collect(Collectors.toList());

        });
    }

    public static void main(String[] args) {

    }
}
