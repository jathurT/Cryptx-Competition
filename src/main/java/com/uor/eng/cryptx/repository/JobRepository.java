package com.uor.eng.cryptx.repository;

import com.uor.eng.cryptx.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
  // Add custom query methods if needed
}
