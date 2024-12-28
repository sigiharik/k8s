package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MemoryLeakApplication {

    private static final List<Object> LEAK = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(MemoryLeakApplication.class, args);
        simulateMemoryLeak();
    }

    public static void simulateMemoryLeak() {
        // Simulate memory leak by adding objects to a list indefinitely
        while (true) {
            LEAK.add(new byte[1024 * 1024]); // 1 MB object to leak
            try {
                Thread.sleep(10); // Slow down the leak slightly for observation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
