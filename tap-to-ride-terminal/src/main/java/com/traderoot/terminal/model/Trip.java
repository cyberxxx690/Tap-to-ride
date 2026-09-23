package com.traderoot.terminal.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * Trip represents a single fare captured by the driver.
 * Each time a rider taps their card, one of these gets created
 * and saved to the local SQLite database. It starts with status
 * "QUEUED" and transitions to "SENT" once the background queue
 * successfully delivers it to the settlement server.
*/
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    private String tripId;
    
    private String riderId;
    private String driverId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String hmacSignature;
    
    // Default to QUEUED so we never lose a trip if the network is down
    private String status = "QUEUED";
    
    // nullable, only set when we assign this trip to a sync batch
    private String batchId;

    /*
     * Required by JPA for reflection-based instantiation
     */
    public Trip() {
    }

    /*
     * Full constructor for manual instantiation when we capture a fare
     */
    public Trip(String tripId, String riderId, String driverId, BigDecimal amount, LocalDateTime timestamp, String hmacSignature, String status, String batchId) {
        this.tripId = tripId;
        this.riderId = riderId;
        this.driverId = driverId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.hmacSignature = hmacSignature;
        this.status = status;
        this.batchId = batchId;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getHmacSignature() {
        return hmacSignature;
    }

    public void setHmacSignature(String hmacSignature) {
        this.hmacSignature = hmacSignature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}
