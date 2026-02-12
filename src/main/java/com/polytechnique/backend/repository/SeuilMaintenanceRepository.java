package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.SeuilMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeuilMaintenanceRepository extends JpaRepository<SeuilMaintenance, Integer> {
}
