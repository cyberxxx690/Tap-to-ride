# Tap-to-Ride Transit System

**Developed as part of my software engineering internship**, this project is a proof-of-concept payment and settlement system built for transit vehicles (like minibuses or taxis). 

This project was built to solve a specific engineering problem: processing digital payments in environments with highly unreliable internet connectivity (like driving through a tunnel or a rural area), while ensuring zero data loss and preventing local database tampering.

## Architecture

The system is split into two independent Spring Boot microservices that communicate via a background syncing queue.

### 1. The Terminal (tap-to-ride-terminal)
- **Role:** Acts as the mobile Point-of-Sale (mPOS) edge device operating inside the vehicle.
- **Core Feature:** Operates entirely offline-first. 
- **How it works:** When a passenger taps their card, the fare is cryptographically signed and saved instantly to a local SQLite database. A background worker (QueueService) constantly monitors the queue and pushes batches of these fares to the Headquarters Server whenever a network connection is available.

### 2. The Headquarters Server (tap-to-ride-server)
- **Role:** Acts as the centralized backend system for fraud detection and financial aggregation.
- **Core Feature:** Centralized fraud detection and settlement aggregation.
- **How it works:** Receives synced batches from terminals, verifies cryptographic signatures to detect any hacked fares, ensures idempotent processing to prevent double-charging, and aggregates individual taps into grouped, daily bank charges to save on credit card processing fees.

## Key Features

- **Offline-First Queue Resilience:** Uses an asynchronous background queue. If the internet drops, the terminal safely holds the transactions locally and automatically syncs them the moment connectivity is restored.
- **HMAC-SHA256 Security:** Every offline tap is sealed using a shared secret key. If a malicious user hacks the local SQLite database to manually lower a fare amount before it syncs, the Headquarters Server automatically catches the forgery during the signature verification step and flags it.
- **Idempotency Guard:** If the terminal loses connectivity *after* sending a batch but *before* receiving the success receipt, it will retry. The server identifies the duplicate Batch ID and safely ignores it, protecting passengers from being double-charged.
- **Settlement Aggregation:** To minimize expensive banking transaction fees, the Settlement Engine automatically groups multiple daily trips by the same rider into a single consolidated Charge record at the end of the day.

## Tech Stack

- **Framework:** Spring Boot 3.2.x (Java 17)
- **Database:** SQLite (embedded for simple local testing on both Terminal and Server)
- **ORM:** Spring Data JPA / Hibernate
- **Frontend:** HTML5, Bootstrap 5, Thymeleaf, Vanilla JS
- **Security:** `javax.crypto.Mac` (HMAC-SHA256)

## How to Run

You will need two terminal windows open to run both services simultaneously.

### Start the Headquarters Server
```bash
cd tap-to-ride-server
mvn spring-boot:run
```
The HQ Dashboard will be available at: http://localhost:8080/console

### Start the Driver's Terminal
```bash
cd tap-to-ride-terminal
mvn spring-boot:run
```
The Driver's UI will be available at: http://localhost:8081

## Testing the Flow (Demo Script)

To fully test the offline architecture, you can follow this sequence:

1. **The Standard Flow:** Open the Terminal UI (`localhost:8081`) and process a payment. Watch the background queue drop to 0. Open the HQ Console (`localhost:8080/console`) to see the pending settlement arrive.
2. **The Tunnel Scenario (Offline Resilience):** Stop the Server (`Ctrl+C`). Process a few more payments on the Terminal. Notice how the UI queue count increases but the driver is never blocked from taking payments. Start the Server back up. Watch the Terminal automatically detect the connection and flush the queue to the Server.
3. **The Settlement:** On the HQ Console, click "Run Settlement Now". The engine will group the pending trips by rider and convert them into final, consolidated bank charges.
