package com.traderoot.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traderoot.server.dto.BatchPayload;
import com.traderoot.server.dto.BatchResponse;
import com.traderoot.server.service.BatchService;

@RestController
@RequestMapping("/api")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping("/batches")

    //Spring boot sees @RequestBody BatchPayload payload and converts the incoming JSON into java object
    public ResponseEntity<BatchResponse> receiveBatch(@RequestBody BatchPayload payload) {
        // TODO [Phase 3] Wire up the Batch Controller
        // 1. Call batchService.processBatch(payload)
        // 2. Return the result wrapped in a ResponseEntity.ok()

        //sends the received batch payload from the api endpoint to the service layer
        BatchResponse response = batchService.processBatch(payload);
        
        //return response back to the terminal through HTTP and receives it via postForEntity in the QueueService class
        return ResponseEntity.ok(response);
    }
}
