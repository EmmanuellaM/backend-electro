package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.UplinkMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UplinkMessageRepository extends JpaRepository<UplinkMessage, Integer> {

    /**
     * Trouve les messages non encore traités, triés par date
     */
    List<UplinkMessage> findByProcessedFalseOrderByCreatedAtAsc();

    /**
     * Trouve les messages par DevEUI
     */
    List<UplinkMessage> findByDevEuiOrderByCreatedAtDesc(String devEui);

    /**
     * Trouve le dernier message d'un dispositif
     */
    Optional<UplinkMessage> findTopByDevEuiOrderByCreatedAtDesc(String devEui);

    /**
     * Compte les messages non traités
     */
    long countByProcessedFalse();
}
