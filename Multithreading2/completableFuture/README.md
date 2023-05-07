# Why Not Futures
1. It cannot be manually completed
2. Multiple futures cannot be Chained together
3. We cannot combine multiple Futures together
4. No proper Exception Handling Mechanism

~~~java
package com.example.completableFuture.FuturesIssues;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Heshan Karunaratne
 */
public class Futures {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(10);
        Future<List<Integer>> future = service.submit(() -> {
            //1. Even though the delay is 1 min this action cannot be manully completed. This action is blocked
            delay(1);
            return Arrays.asList(1, 2, 3, 4);
        });

        List<Integer> list = future.get();
        System.out.println(list);
    }

    private static void delay(int min) {
        try {
            TimeUnit.MINUTES.sleep(min);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
~~~

~~~java
package com.example.completableFuture.FuturesIssues;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Futures {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(10);
        Future<List<Integer>> future1 = service.submit(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
            return Arrays.asList(1, 2, 3, 4);
        });

        Future<List<Integer>> future2 = service.submit(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
            return Arrays.asList(1, 2, 3, 4);
        });
        // 3. Cannot combine multiple Futures together
        List<Integer> list = future1.get();
        List<Integer> list2 = future2.get();

    }
}
~~~

# CompletableFuture

- runAsync(): If you want to run task asynchronously and do not want to return anything from that task use runAsync()

~~~java
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
~~~

- supplyAsync(): If you want to run task asynchronously and want to return anything from that task use supplyAsync()

~~~java
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
        supplyAsyncDemo.getEmployees();
    }

}
~~~

- thenApplyAsync(), thenAcceptAsync(), thenRunAsync()
~~~java
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
~~~

# Functions

- Function
~~~java
package com.example.JavaFunctionals.FunctionalInterface;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Heshan Karunaratne
 */
public class _Function {
    public static void main(String[] args) {
        int increment = incrementByOne(0);
        System.out.println(increment);

        Function<Integer, Integer> addOneAndThenMultiplyByTen = incrementByOneFunction.andThen(multiplyByTenFunction);
        Integer apply = addOneAndThenMultiplyByTen.apply(0);
        System.out.println(apply);

        int first = incrementByOneAndMultiplyByTen(1, 10);

        System.out.println("first: " + first);
        Integer second = incrementByOneAndMultiplyByTenFunction.apply(1, 10);
        System.out.println("second: " + second);
    }

    static Function<Integer, Integer> incrementByOneFunction = number -> number + 1;

    static Function<Integer, Integer> multiplyByTenFunction = number -> number * 10;

    static int incrementByOne(int num) {
        return num + 1;
    }

    static int incrementByOneAndMultiplyByTen(int num, int num2) {
        return (num + 1) * num2;
    }

    static BiFunction<Integer, Integer, Integer> incrementByOneAndMultiplyByTenFunction = (num, num2) -> (num + 1) * num2;
}
~~~

- Consumer
~~~java
package com.example.JavaFunctionals.FunctionalInterface;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Heshan Karunaratne
 */
public class _Consumer {
    public static void main(String[] args) {
        greetCustomer(new Customer("Heshan", "1234567890"));
        greetCustomerConsumer.accept(new Customer("Heshan2", "1234567890"));
        greetCustomerConsumerV2.accept(new Customer("Heshan3", "1234567890"), true);
        greetCustomerConsumerV2.accept(new Customer("Heshan4", "1234567890"), false);
    }

    static BiConsumer<Customer, Boolean> greetCustomerConsumerV2 = (customer, showPhoneNumber) ->
            System.out.println("Hello " + customer.customerName
                    + ", thanks for registering phone number "
                    + (showPhoneNumber ? customer.customerPhoneNumber : "**********"));

    static Consumer<Customer> greetCustomerConsumer = customer -> System.out.println("Hello " + customer.customerName
            + ", thanks for registering phone number "
            + customer.customerPhoneNumber);

    static void greetCustomer(Customer customer) {
        System.out.println("Hello " + customer.customerName
                + ", thanks for registering phone number "
                + customer.customerPhoneNumber);
    }

    @Data
    @AllArgsConstructor
    static class Customer {
        private final String customerName;
        private final String customerPhoneNumber;
    }
}
~~~