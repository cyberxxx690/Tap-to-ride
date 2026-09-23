package com.traderoot.server.dto;

import java.util.List;

/*
 * Wraps up a collection of trips sent by a terminal during its sync interval.
 * Includes a unique batchId that we can use for tracking and idempotency.
 */
public class BatchPayload {

    private String batchId;
    private String terminalId;
    private List<TripPayload> trips;

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
