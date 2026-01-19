package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.SmsMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour accéder aux SMS
 */
@Repository
public interface SmsMessageRepository extends JpaRepository<SmsMessage, Integer> {

    /**
     * Récupérer tous les SMS envoyés à un infirmier, triés par date décroissante
     */
    List<SmsMessage> findByInfirmierLocalIdOrderBySentAtDesc(int infirmierId);

    /**
     * Compter le nombre de SMS envoyés à un infirmier
     */
    long countByInfirmierLocalId(int infirmierId);
}
