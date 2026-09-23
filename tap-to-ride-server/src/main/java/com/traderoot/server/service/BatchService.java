package com.traderoot.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.traderoot.server.dto.BatchPayload;
import com.traderoot.server.dto.BatchResponse;
import com.traderoot.server.dto.TripPayload;
import com.traderoot.server.model.Batch;
import com.traderoot.server.model.Trip;
import com.traderoot.server.repository.BatchRepository;
import com.traderoot.server.repository.TripRepository;


/*
 * BatchService handles the heavy lifting when new trip batches arrive from terminals.
 * It ensures we process idempotently, validates each trip's signature, and stores the records.
 */
@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final TripRepository tripRepository;
    private final HmacVerifier hmacVerifier;

    public BatchService(BatchRepository batchRepository, TripRepository tripRepository, HmacVerifier hmacVerifier) {
        this.batchRepository = batchRepository;
        this.tripRepository = tripRepository;
        this.hmacVerifier = hmacVerifier;
    }

    public BatchResponse processBatch(BatchPayload payload) {
        // TODO [Phase 5] Implement the idempotency check here
        // 1. Check idempotency: if batchRepository.existsByBatchId(payload.getBatchId()) return success immediately

        // IDEMPOTENCY GUARD: If the terminal loses internet after sending this batch, it will try to send it again.
        // We check if we already processed this exact batch box. If yes, we just smile, nod, and return success
        // so the terminal stops retrying, protecting the riders from getting double-charged!
        if (batchRepository.existsByBatchId(payload.getBatchId())) {

            BatchResponse response = new BatchResponse();

            response.setBatchId(payload.getBatchId());
            response.setStatus("DUPLICATE");
            response.setMessage("This batch was already received");
            response.setProcessedCount(0);

            return response;

        } else {
            // If the batch is new, we will process it
            
            // Create a new Batch entity and save it
            Batch batch = new Batch();
            batch.setBatchId(payload.getBatchId());
            batch.setTerminalId(payload.getTerminalId());
            batch.setReceivedAt(LocalDateTime.now());
            batchRepository.save(batch);

            int validCount = 0;
            int fraudCount = 0;
            List<String> flaggedTripIds = new ArrayList<>();

            for (TripPayload trip : payload.getTrips()) {
                //The Server recalculate the expected signature using the trip data and the shared secret key
                boolean isSignatureValid = hmacVerifier.verify(trip.getRiderId(), trip.getAmount(), trip.getTimestamp(), trip.getHmacSignature());
                
                
                //create new trip and save the field in the server database
                Trip newTrip = new Trip();
                newTrip.setTripId(trip.getTripId());
                newTrip.setRiderId(trip.getRiderId());
                newTrip.setDriverId(trip.getDriverId());
                newTrip.setAmount(trip.getAmount());
                newTrip.setTimestamp(LocalDateTime.parse(trip.getTimestamp()));
                newTrip.setHmacSignature(trip.getHmacSignature());
                newTrip.setSignatureValid(isSignatureValid);
                newTrip.setBatchId(payload.getBatchId());

               
                
                if (!isSignatureValid) {
                    // FRAUD DETECTED! We do not delete it (we want forensic evidence).
                    // We flag it so it gets ignored during the nightly settlement process.
                    newTrip.setStatus("FLAGGED");
                    fraudCount++;
                    flaggedTripIds.add(trip.getTripId());
                    System.err.println("SECURITY ALERT: Tampered trip detected: " + trip.getTripId());
                    
                } else {
                    // Safe and verified. Ready to be billed tonight!
                    newTrip.setStatus("RECEIVED");
                    validCount++;
                }

                tripRepository.save(newTrip);

                
            }


            //create the response to foward to the Batch Controller
            BatchResponse response = new BatchResponse();
            response.setBatchId(payload.getBatchId());
            response.setProcessedCount(validCount);
            response.setFlaggedTripIds(flaggedTripIds);
           

            if (fraudCount > 0) {
                response.setStatus("PARTIAL");
                response.setMessage(validCount + " trips accepted, " + fraudCount + " trips flagged for fraud");
            } else {
                response.setStatus("ACCEPTED");
                response.setMessage("All trips verified and accepted");
            }

            return response;


            







                




            
            

        }
        
        // TODO [Phase 5] Implement batch processing logic
        // 2. Create Batch record, save it
        // 3. For each trip in payload: verify HMAC signature, create Trip entity, set signatureValid, save
        // 4. Return BatchResponse with status

        
        
       
    }
}
