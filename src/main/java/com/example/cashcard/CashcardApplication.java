// Package declaration
package com.example.cashcard;

// Import statements
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Annotation to indicate that this class is a Spring Boot application
@SpringBootApplication
public class CashcardApplication {

    // Main method, the entry point of the application
    public static void main(String[] args) {
        // Start the Spring Boot application using the CashcardApplication class
        SpringApplication.run(CashcardApplication.class, args);
    }

}
