package com.traderoot.server.repository;

import com.traderoot.server.model.ChargeLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Repository for pulling line items for our aggregated charges.
 */
@Repository
public interface ChargeLineRepository extends JpaRepository<ChargeLine, String> {
    
    List<ChargeLine> findByCharge_ChargeId(String chargeId);
}
