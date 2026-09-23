package com.traderoot.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/*
 * Batch represents a collection of trips sent by a terminal.
 * We store batches to keep track of what has been synced and to prevent processing
 * the same batch twice. The batchId has a UNIQUE constraint which acts as our idempotency key.
 */
@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @Column(unique = true)
    private String batchId;
    
    private String terminalId;
    private LocalDateTime receivedAt;

    public Batch() {
    }

    public Batch(String batchId, String terminalId, LocalDateTime receivedAt) {
        this.batchId = batchId;
        this.terminalId = terminalId;
        this.receivedAt = receivedAt;
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

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}
