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
        Future<List<Integer>> future1 = service.submit(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
            System.out.println(10/0);
            return Arrays.asList(1, 2, 3, 4);
        });


        List<Integer> list = future1.get();
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
