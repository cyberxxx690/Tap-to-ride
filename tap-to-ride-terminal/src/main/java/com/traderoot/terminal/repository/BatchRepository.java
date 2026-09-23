package com.traderoot.terminal.repository;

import com.traderoot.terminal.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/*
 * Handles database operations for the Batch entity.
 * Keeps track of which network batches are still pending or failed.
 */
public interface BatchRepository extends JpaRepository<Batch, String> {

    /*
     * Generates a query to find all batches matching a specific status.
     * We can use this to retry batches that are stuck in the "PENDING" state.
     */
    List<Batch> findByStatus(String status);
}
