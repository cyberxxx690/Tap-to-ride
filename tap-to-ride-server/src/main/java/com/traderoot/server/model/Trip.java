package com.traderoot.server.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * Trip represents a single fare captured by the driver.
 * This is the SERVER side Trip entity, received from the terminal as part of a batch.
 * It tracks the status of the trip on the server, transitioning from RECEIVED to SETTLED.
 * Note the extra signatureValid field to verify data integrity.
 */
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    private String tripId;
    
    private String batchId;
    private String riderId;
    private String driverId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String hmacSignature;
    private Boolean signatureValid;
    
    // Status starts as RECEIVED and becomes SETTLED when grouped into a charge
    private String status = "RECEIVED";

    public Trip() {
    }

    public Trip(String tripId, String batchId, String riderId, String driverId, BigDecimal amount, LocalDateTime timestamp, String hmacSignature, Boolean signatureValid, String status) {
        this.tripId = tripId;
        this.batchId = batchId;
        this.riderId = riderId;
        this.driverId = driverId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.hmacSignature = hmacSignature;
        this.signatureValid = signatureValid;
        this.status = status;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
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

    public Boolean getSignatureValid() {
        return signatureValid;
    }

    public void setSignatureValid(Boolean signatureValid) {
        this.signatureValid = signatureValid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
