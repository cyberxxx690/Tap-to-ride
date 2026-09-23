package com.traderoot.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * ChargeLine provides the itemized breakdown for a day's charge.
 * Each line references one individual trip that contributes to the parent charge's total amount.
 */
@Entity
@Table(name = "charge_lines")
public class ChargeLine {

    @Id
    private String lineId;
    
    private String tripId;

    @ManyToOne
    @JoinColumn(name = "charge_id")
    private Charge charge;

    public ChargeLine() {
    }

    public ChargeLine(String lineId, String tripId, Charge charge) {
        this.lineId = lineId;
        this.tripId = tripId;
        this.charge = charge;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }
}
