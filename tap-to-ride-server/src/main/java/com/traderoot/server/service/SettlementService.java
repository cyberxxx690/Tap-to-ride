package com.traderoot.server.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.traderoot.server.model.Charge;
import com.traderoot.server.model.ChargeLine;
import com.traderoot.server.model.Trip;
import com.traderoot.server.repository.ChargeLineRepository;
import com.traderoot.server.repository.ChargeRepository;
import com.traderoot.server.repository.TripRepository;



/*
 * SettlementService aggregates pending trips for the day into comprehensive Charge records.
 * This runs at the end of the operating window to compute exactly what to bill each rider.
 */
@Service
public class SettlementService {

    private final TripRepository tripRepository;
    private final ChargeRepository chargeRepository;
    private final ChargeLineRepository chargeLineRepository;

    public SettlementService(TripRepository tripRepository, ChargeRepository chargeRepository, ChargeLineRepository chargeLineRepository) {
        this.tripRepository = tripRepository;
        this.chargeRepository = chargeRepository;
        this.chargeLineRepository = chargeLineRepository;
    }

    public List<Charge> settleTrips(LocalDate date) {
        // TODO [Phase 6] Implement the aggregation logic
        // 1. Fetch all trips with status "RECEIVED"
        // 2. Group those trips by riderId
        // 3. For each rider, sum the amounts and create a Charge record
        // 4. Create a ChargeLine for each trip pointing back to the Charge
        // 5. Mark the processed trips as "SETTLED"
        // 6. Return the list of created charges

        //GET the TRIPS at the end of the day
        //it takes teach returned row and converts into a Trip java object

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Trip> trips = tripRepository.findByStatusAndTimestampGreaterThanEqualAndTimestampLessThan("RECEIVED", start, end);
        Map<String, List<Trip>> tripsByRider = trips.stream().collect(Collectors.groupingBy(Trip::getRiderId));
        
        //this will store the list of charge objcts
        List<Charge> createdCharges = new ArrayList<>();

        for (Map.Entry<String, List<Trip>> entry : tripsByRider.entrySet()) {

            String riderId = entry.getKey(); //get riderId
            List<Trip> riderTrips = entry.getValue(); //the trips inside the drawer


            BigDecimal totalAmount = BigDecimal.ZERO;

            for (Trip trip : riderTrips) {
                //add the total biling
                totalAmount = totalAmount.add(trip.getAmount());
            }

             //Create a new charge which is a database record that tells us
            //For example How much a riderOwes it is the record that will be sent to a payment processor
            Charge charge = new Charge();
            

            charge.setChargeId(UUID.randomUUID().toString());
            charge.setRiderId(riderId);
            charge.setChargeDate(date); //today's date
            charge.setTotalAmount(totalAmount);
            charge.setCreatedAt(LocalDateTime.now());
            
            Charge savedCharge  =  chargeRepository.save(charge);
            createdCharges.add(savedCharge);
           


            // this creates a Charge_Line record which is meta data about each trip
            for (Trip trip : riderTrips) {
                ChargeLine line = new ChargeLine();

                line.setLineId(UUID.randomUUID().toString());
                line.setCharge(savedCharge); //links the line to riders Charge
                line.setTripId(trip.getTripId());
                chargeLineRepository.save(line);

            }
            

            // update the trips to settled in the trips table
            for (Trip trip : riderTrips) {
                trip.setStatus("SETTLED");
            }

            tripRepository.saveAll(riderTrips);




        





        }
       



        return createdCharges;
    }

    public List<Charge> getChargesForDate(LocalDate date) {
        // TODO [Phase 7] Retrieve charges from the repository
        return chargeRepository.findByChargeDate(date);
    }
}
