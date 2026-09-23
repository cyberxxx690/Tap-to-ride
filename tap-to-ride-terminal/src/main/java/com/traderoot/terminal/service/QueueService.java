package com.traderoot.terminal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.traderoot.terminal.dto.BatchPayload;
import com.traderoot.terminal.dto.BatchResponse;
import com.traderoot.terminal.dto.TripPayload;
import com.traderoot.terminal.model.Batch;
import com.traderoot.terminal.model.Trip;
import com.traderoot.terminal.repository.BatchRepository;
import com.traderoot.terminal.repository.TripRepository;


/*
 * Background worker that looks for unsynced fares and tries to push them to the server.
 * This is crucial because terminals operate inside vehicles where the internet connection
 * drops frequently. By queuing locally and retrying, we never lose a trip.
 */
@Service
public class QueueService {

    private final TripRepository tripRepository;
    private final BatchRepository batchRepository;
    private final RestTemplate restTemplate;

    @Value("${app.server.url}")
    private String serverUrl;
    
    @Value("${app.terminal.id}")
    private String terminalId;

    public QueueService(TripRepository tripRepository, BatchRepository batchRepository, RestTemplate restTemplate) {
        this.tripRepository = tripRepository;
        this.batchRepository = batchRepository;
        this.restTemplate = restTemplate;
    }

    /*
     * Runs every 10 seconds to find queued trips and send them off.
     * We rely on Spring's scheduling framework to fire this continuously.
     */
    @Scheduled(fixedRate = 10000)
    public void syncTrips() {

        // 1. Find all trips where status == "QUEUED"
        // 2. If list is empty, just return early
        // 3. Create a new Batch entity, save it (status="PENDING")
        // 4. Update the batchId on all those trips and save them
        // 5. Convert the Trips into TripPayload objects
        // 6. Create a BatchPayload
        // 7. Use a try/catch block to send a POST request via restTemplate
        //    -> If success: update Batch and Trips to "SENT"
        //    -> If exception (network down): increment Batch retryCount and print a warning


        //stores a list of trips that are queued as objects
        List<Trip> queuedTrips = tripRepository.findByStatus("QUEUED");

        //check if there are any trips in the list if not return early
        if (queuedTrips.isEmpty()){
            return;
        }
        
        //create a new batch
        Batch batch = new Batch();

        //capture the batch entries and save to the batch table
        batch.setBatchId(UUID.randomUUID().toString());
        batch.setCreatedAt(LocalDateTime.now());
        batch.setStatus("PENDING");
        batchRepository.save(batch);

        //update the batchId and the status in the Trips Table 
        for (Trip trips : queuedTrips) {
            trips.setBatchId(batch.getBatchId());
            trips.setStatus("PENDING");

        }
        //save all the trips with the updated batchId and status to the trips table
         tripRepository.saveAll(queuedTrips);
        
        //Create an array to make a copy of the trips 
        List<TripPayload> tripPayloads = new ArrayList<>();

        //add entries to the copies
        for (Trip payload_trips : queuedTrips) {
            TripPayload t_payload = new TripPayload();

            t_payload.setTripId(payload_trips.getTripId());
            t_payload.setRiderId(payload_trips.getRiderId());
            t_payload.setDriverId(payload_trips.getDriverId());
            t_payload.setAmount(payload_trips.getAmount());
            t_payload.setTimestamp(payload_trips.getTimestamp().toString());
            t_payload.setHmacSignature(payload_trips.getHmacSignature());
            
            //add the object to the list of tripPayloads 
            tripPayloads.add(t_payload);

        }

        //add the batchId and terminalId to the payload
        BatchPayload batchPayload = new BatchPayload();
        batchPayload.setBatchId(batch.getBatchId());
        batchPayload.setTerminalId(terminalId);
        batchPayload.setTrips(tripPayloads);


        try {
            // It takes your BatchPayload (the shipping box), converts it into a JSON string, and fires a POST request to the Server.
            ResponseEntity<BatchResponse> response = restTemplate.postForEntity(serverUrl + "/api/batches", batchPayload, BatchResponse.class);
            BatchResponse batchResponse = response.getBody();

            //if the server responds with an HTTP 200 OK header  and if the response body exists
            if (response.getStatusCode().is2xxSuccessful() && batchResponse != null) {
                //update the batch and trips to SENT
                List<String> flaggedTripIds = batchResponse.getFlaggedTripIds();

                for (Trip trips : queuedTrips) {
                    //check if the List contains the flaggedTripId
                    if (flaggedTripIds != null && flaggedTripIds.contains(trips.getTripId())) {
                        trips.setStatus("FLAGGED");
                    } else {
                        // if not then nothing was flagged and everything was sent
                        trips.setStatus("SENT");
                    }
                   
                    
                }
                batch.setStatus(batchResponse.getStatus());
                batchRepository.save(batch);
                tripRepository.saveAll(queuedTrips); // Save all at once!

            } else {
                // handle condition when reponse body is null
                batch.setStatus("FAILED");
                batch.setRetryCount(batch.getRetryCount() + 1);
                batchRepository.save(batch);

                for (Trip trip : queuedTrips) {
                    trip.setStatus("QUEUED");
                }

                tripRepository.saveAll(queuedTrips);

                System.err.println(
                        "Server returned an invalid response for Batch "
                                + batch.getBatchId());

            }
            
        } catch (RestClientException exception) {
            // Could not communicate with the server it increments retry count, saves it and will try again later
            batch.setStatus("FAILED");
            batch.setRetryCount(batch.getRetryCount() + 1);
            //save the batch status into the batch table
            batchRepository.save(batch);

            //if the network is down, we will set the status of the trips back to QUEUED so that they can be retried later
            for (Trip trips : queuedTrips) {
                trips.setStatus("QUEUED");
            }
            tripRepository.saveAll(queuedTrips); // Save all at once!

            System.err.println("Delivery failed for Batch " + batch.getBatchId() + 
                       ". Reason: " + exception.getMessage());


        }
        
        
    }
}
