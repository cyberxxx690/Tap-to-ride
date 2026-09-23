package com.traderoot.terminal.dto;

import java.util.List;

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
