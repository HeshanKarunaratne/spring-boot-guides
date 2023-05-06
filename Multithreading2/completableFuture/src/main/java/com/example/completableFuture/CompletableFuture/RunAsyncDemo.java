package com.example.completableFuture.CompletableFuture;

import com.example.completableFuture.dto.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Heshan Karunaratne
 */
public class RunAsyncDemo {
    public Void saveEmployees(File jsonFile) throws ExecutionException, InterruptedException {

        ObjectMapper mapper = new ObjectMapper();

        CompletableFuture<Void> runAsyncFuture = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Employee> employees = mapper.readValue(jsonFile, new TypeReference<List<Employee>>() {
                    });
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    System.out.println(employees.size());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return runAsyncFuture.get();
    }

    public Void saveEmployeesLambda(File jsonFile) throws ExecutionException, InterruptedException {

        ObjectMapper mapper = new ObjectMapper();
        Executor executor = Executors.newFixedThreadPool(5);
        CompletableFuture<Void> runAsyncFuture = CompletableFuture.runAsync(() -> {
            try {
                List<Employee> employees = mapper.readValue(jsonFile, new TypeReference<List<Employee>>() {
                });
                System.out.println("Thread: " + Thread.currentThread().getName());
                System.out.println(employees.size());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }, executor);

        return runAsyncFuture.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RunAsyncDemo runAsyncDemo = new RunAsyncDemo();
        runAsyncDemo.saveEmployees(new File("employees.json"));
        runAsyncDemo.saveEmployeesLambda(new File("employees.json"));
    }

}
