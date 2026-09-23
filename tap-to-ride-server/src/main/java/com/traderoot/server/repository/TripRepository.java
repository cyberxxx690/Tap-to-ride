package com.traderoot.server.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traderoot.server.model.Trip;

/*
 * Repository for managing Trip entities in the database.
 * We include some custom finders to help pull out trips by their processing state
 * or batch association.
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, String> {
    
    List<Trip> findByStatus(String status);
    
    List<Trip> findByBatchId(String batchId);
    
    List<Trip> findByRiderIdAndStatus(String riderId, String status);

    List<Trip> findByStatusAndTimestampGreaterThanEqualAndTimestampLessThan(
            String status,
            LocalDateTime start,
            LocalDateTime end);

    long countByStatus(String status);

    // TODO [Phase 7] Add custom @Query for aggregating trips by riderId and date
}
