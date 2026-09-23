package com.traderoot.terminal.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.traderoot.terminal.model.Trip;
import com.traderoot.terminal.repository.TripRepository;

/*
 * Responsible for handling new fares when a rider taps their card.
 * We encapsulate the business logic here so the web controllers remain clean.
 */
@Service
public class FareService {

    private final TripRepository tripRepository;
    private final HmacSigner hmacSigner;

    public FareService(TripRepository tripRepository, HmacSigner hmacSigner) {
        this.tripRepository = tripRepository;
        this.hmacSigner = hmacSigner;
    }

    /*
     * Generates a new trip, signs it, and queues it up for background delivery.
     * We don't try to sync over the network immediately because connectivity might be bad.
     */
    public Trip captureFare(String riderId, String driverId, BigDecimal amount) {
        // TODO [Phase 1] Implement the fare saving logic here
        // 1. Generate a new UUID for the trip_id
        // 2. Get the current LocalDateTime
        // 3. Call hmacSigner.sign() to cryptographically seal the data
        // 4. Create a new Trip entity and populate its fields
        // 5. Set the status to "QUEUED"
        // 6. Save using tripRepository and return the saved trip

        UUID uuid = UUID.randomUUID();
        
        // Truncate to SECONDS to prevent SQLite from losing nanosecond precision 
        // which causes the HMAC signatures to mismatch on the server!
        LocalDateTime time = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);


        //makes the signature
        String hash = hmacSigner.sign(riderId, amount, time);


        //creates a new trip and records the components
        Trip trip  = new Trip();

        trip.setTripId(uuid.toString());
        trip.setRiderId(riderId);
        trip.setDriverId(driverId);
        trip.setAmount(amount);
        trip.setTimestamp(time);
        trip.setHmacSignature(hash);
        trip.setStatus("QUEUED");


        // tripRepository use JPA which is the spring boot ORM to record and write the sql for you in the database
        return tripRepository.save(trip);





    }

    /*
     * We expose this to easily display the pending count on the user interface.
     */
    public long getQueuedCount() {
        // We count both QUEUED and PENDING because when the QueueService is actively 
        // trying to send them over the network, it temporarily marks them as PENDING.
        return tripRepository.countByStatus("QUEUED") + tripRepository.countByStatus("PENDING");
    }
}
