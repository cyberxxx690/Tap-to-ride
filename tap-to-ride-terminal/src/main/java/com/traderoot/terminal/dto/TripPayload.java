package com.traderoot.terminal.dto;

import java.math.BigDecimal;

/*
 * A plain network data transfer object that matches the JSON shape expected by the server.
 * We separate this from the database entity to avoid coupling our internal schema
 * to our external API contracts.
 */
public class TripPayload {
    
    private String tripId;
    private String riderId;
    private String driverId;
    private BigDecimal amount;
    private String timestamp;
    private String hmacSignature;

    public TripPayload() {
    }

    public TripPayload(String tripId, String riderId, String driverId, BigDecimal amount, String timestamp, String hmacSignature) {
        this.tripId = tripId;
        this.riderId = riderId;
        this.driverId = driverId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.hmacSignature = hmacSignature;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHmacSignature() {
        return hmacSignature;
    }

    public void setHmacSignature(String hmacSignature) {
        this.hmacSignature = hmacSignature;
    }
}
