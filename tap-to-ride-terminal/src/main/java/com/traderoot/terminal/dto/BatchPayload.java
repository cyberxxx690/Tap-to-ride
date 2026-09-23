package com.traderoot.terminal.dto;

import java.util.List;

/*
 * Wraps multiple trips into one network delivery package.
 * This is much more efficient than sending dozens of HTTP requests
 * one by one over a slow mobile connection.
 */
public class BatchPayload {

    private String batchId;
    private String terminalId;
    private List<TripPayload> trips;

    public BatchPayload() {
    }

    public BatchPayload(String batchId, String terminalId, List<TripPayload> trips) {
        this.batchId = batchId;
        this.terminalId = terminalId;
        this.trips = trips;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public List<TripPayload> getTrips() {
        return trips;
    }

    public void setTrips(List<TripPayload> trips) {
        this.trips = trips;
    }
}
