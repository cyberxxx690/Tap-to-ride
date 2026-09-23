package com.traderoot.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * A Charge groups all of a rider's individual trips for a single day into one billable record.
 * Rather than charging a credit card every time someone gets on a bus, we batch them up
 * at the end of the day to save on transaction fees.
 */
@Entity
@Table(name = "charges")
public class Charge {

    @Id
    private String chargeId;
    
    private String riderId;
    private LocalDate chargeDate;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "charge")
    private List<ChargeLine> lines = new ArrayList<>();

    public Charge() {
    }

    public Charge(String chargeId, String riderId, LocalDate chargeDate, BigDecimal totalAmount, LocalDateTime createdAt) {
        this.chargeId = chargeId;
        this.riderId = riderId;
        this.chargeDate = chargeDate;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public LocalDate getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(LocalDate chargeDate) {
        this.chargeDate = chargeDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ChargeLine> getLines() {
        return lines;
    }

    public void setLines(List<ChargeLine> lines) {
        this.lines = lines;
    }
}
