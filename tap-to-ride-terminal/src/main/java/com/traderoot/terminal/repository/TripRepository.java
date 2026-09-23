package com.traderoot.terminal.repository;

import com.traderoot.terminal.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/*
 * We use this repository to store and retrieve Trip entities from our SQLite database.
 * Spring Data JPA implements these methods automatically based on the naming convention.
 */
public interface TripRepository extends JpaRepository<Trip, String> {

    /*
     * Generates a query to find all trips matching a specific status.
     * We use this to fetch all "QUEUED" trips when it is time to sync them.
     */
    List<Trip> findByStatus(String status);

    /*
     * Generates a query to find all trips that were assigned to a particular batch.
     * We use this to easily mark them as "SENT" once the batch succeeds.
     */
    List<Trip> findByBatchId(String batchId);

    /*
     * Generates a query to simply count how many trips have a specific status.
     * Useful for displaying the number of pending unsynced trips on the terminal screen.
     */
    long countByStatus(String status);
}
