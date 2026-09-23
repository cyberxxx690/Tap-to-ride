package com.traderoot.terminal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * This is Process 1 of our ecosystem running on port 8081.
 * It acts as the local fare collection terminal inside the vehicle.
 * We enable scheduling here so that our background queues can automatically
 * retry sending captured trips to the central settlement server.
 */
@SpringBootApplication
@EnableScheduling
public class TerminalApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerminalApplication.class, args);
    }
}
