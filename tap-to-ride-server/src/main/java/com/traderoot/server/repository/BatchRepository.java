package com.traderoot.server.repository;

import com.traderoot.server.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Repository for managing Batch entities.
 * We use this heavily during the sync process to verify we aren't double-processing batches.
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, String> {
    
    // Checks if we've already received and processed a specific batch
    boolean existsByBatchId(String batchId);
}
