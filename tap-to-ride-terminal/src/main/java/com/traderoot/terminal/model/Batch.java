package com.traderoot.terminal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/*
 * Batch helps us group multiple trips together for network delivery.
 * We track the lifecycle of the delivery itself here. It starts as PENDING,
 * and if the server responds successfully, we mark it SENT. If it fails,
 * we increment the retry count and eventually might mark it FAILED.
 */
@Entity
@Table(name = "batches")
public class Batch {

    @Id
    private String batchId;
    
    private LocalDateTime createdAt;
    
    // Starts as PENDING before network attempt
    private String status = "PENDING";
    
    private int retryCount = 0;

    /*
     * Required by JPA for reflection-based instantiation
     */
    public Batch() {
    }

    /*
     * Full constructor for manual instantiation when we prepare a sync
     */
    public Batch(String batchId, LocalDateTime createdAt, String status, int retryCount) {
        this.batchId = batchId;
        this.createdAt = createdAt;
        this.status = status;
        this.retryCount = retryCount;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
