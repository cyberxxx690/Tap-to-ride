package com.traderoot.server.repository;

import com.traderoot.server.model.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/*
 * Repository for managing aggregated Charge entities.
 */
@Repository
public interface ChargeRepository extends JpaRepository<Charge, String> {
    
    List<Charge> findByChargeDate(LocalDate date);
    
    List<Charge> findByRiderId(String riderId);
}
