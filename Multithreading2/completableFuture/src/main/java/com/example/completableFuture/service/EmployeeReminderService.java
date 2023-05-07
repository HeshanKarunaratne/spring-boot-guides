package com.example.completableFuture.service;

import com.example.completableFuture.database.EmployeeDatabase;
import com.example.completableFuture.dto.Employee;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Heshan Karunaratne
 */
public class EmployeeReminderService {

    public static Void sendReminderToEmployee() throws ExecutionException, InterruptedException {
        Executor executor = Executors.newFixedThreadPool(10);
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {

            System.out.println("FetchEmployee: " + Thread.currentThread().getName());
            return EmployeeDatabase.fetchEmployees();

        }, executor).thenApplyAsync(employees -> {

            System.out.println("FilterNewEmployee: " + Thread.currentThread().getName());
            return employees
                    .stream()
                    .filter(employee -> "TRUE".equalsIgnoreCase(employee.getNewJoiner()))
                    .collect(Collectors.toList());

        }, executor).thenApplyAsync(employees -> {

            System.out.println("FilterTrainingNotComplete: " + Thread.currentThread().getName());
            return employees
                    .stream()
                    .filter(employee -> "TRUE".equalsIgnoreCase(employee.getLearningPending()))
                    .collect(Collectors.toList());

        }, executor).thenApplyAsync(employees -> {

            System.out.println("GetEmails: " + Thread.currentThread().getName());
            return employees.stream().map(Employee::getEmail)
                    .collect(Collectors.toList());

        }, executor).thenAcceptAsync(emails -> {

            System.out.println("SendEmail: " + Thread.currentThread().getName());
            emails.forEach(EmployeeReminderService::sendEmail);

        }, executor);
        return voidCompletableFuture.get();
    }

    public static void sendEmail(String email) {
        System.out.println("Sending training reminder email to: " + email);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        sendReminderToEmployee();
        long end = System.currentTimeMillis();
        System.out.println("Time executed: " + (end - start) + "ms");
    }
}
