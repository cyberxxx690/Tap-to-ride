package com.traderoot.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * The main entry point for the Tap to Ride Settlement Server.
 * This acts as Process 2 in the system architecture and runs on port 8080.
 * It is responsible for receiving batches from the terminal, verifying signatures,
 * and processing settlements.
 */
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
