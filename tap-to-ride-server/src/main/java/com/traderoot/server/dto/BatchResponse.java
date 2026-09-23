package com.traderoot.server.dto;
import java.util.List;

/*
 * The server's response to an incoming batch payload.
 * Lets the terminal know if we accepted the payload or if it was a duplicate.
 */
public class BatchResponse {

    private String batchId;
    private String status;
    private String message;
    private int processedCount;
    private List<String> flaggedTripIds;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(int processedCount) {
        this.processedCount = processedCount;
    }

    public List<String> getFlaggedTripIds() {
        return flaggedTripIds;
    }

    public void setFlaggedTripIds(List<String> flaggedTripIds) {
        this.flaggedTripIds = flaggedTripIds;
    } 
}
