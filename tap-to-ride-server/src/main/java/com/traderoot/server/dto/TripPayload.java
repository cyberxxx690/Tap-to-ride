package com.traderoot.server.dto;

import java.math.BigDecimal;

/*
 * A plain POJO for incoming trip data.
 * Must precisely match the JSON structure built by the terminal's sync routine.
 */
public class TripPayload {

    private String tripId;
    private String riderId;
    private String driverId;
    private BigDecimal amount;
    private String timestamp;
    private String hmacSignature;

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
