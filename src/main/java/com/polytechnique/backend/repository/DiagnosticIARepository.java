package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.DiagnosticIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité DiagnosticIA
 */
@Repository
public interface DiagnosticIARepository extends JpaRepository<DiagnosticIA, Integer> {

    /**
     * Trouve un diagnostic IA par l'ID des paramètres
     */
    Optional<DiagnosticIA> findByParametresId(Integer parametresId);

    /**
     * Trouve tous les diagnostics IA pour un patient donné
     */
    List<DiagnosticIA> findByParametres_IdentifiantPatientOrderByCreatedAtDesc(String identifiantPatient);

    /**
     * Trouve tous les diagnostics IA validés par un médecin
     */
    List<DiagnosticIA> findByMedecinValidateurIdOrderByCreatedAtDesc(Integer medecinId);

    /**
     * Trouve tous les diagnostics IA en attente de validation
     */
    List<DiagnosticIA> findByValideParMedecinIsNullOrderByCreatedAtDesc();

    /**
     * Trouve tous les diagnostics IA d'une classe spécifique
     */
    List<DiagnosticIA> findByClassePrediteOrderByCreatedAtDesc(String classePredite);

    /**
     * Compte le nombre de diagnostics IA pour un patient
     */
    Long countByParametres_IdentifiantPatient(String identifiantPatient);
}
