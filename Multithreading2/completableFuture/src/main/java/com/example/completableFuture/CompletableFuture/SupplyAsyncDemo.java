package com.example.completableFuture.CompletableFuture;

import com.example.completableFuture.database.EmployeeDatabase;
import com.example.completableFuture.dto.Employee;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Heshan Karunaratne
 */
public class SupplyAsyncDemo {

    public List<Employee> getEmployees() throws ExecutionException, InterruptedException {

        CompletableFuture<List<Employee>> listCompletableFuture =
                CompletableFuture.supplyAsync(() -> EmployeeDatabase.fetchEmployees());
        return listCompletableFuture.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SupplyAsyncDemo supplyAsyncDemo = new SupplyAsyncDemo();
        List<Employee> employees = supplyAsyncDemo.getEmployees();
        employees.forEach(System.out::println);
    }

}
