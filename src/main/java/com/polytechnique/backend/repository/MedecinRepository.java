package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Medecin
 * JpaRepository fournit automatiquement les méthodes CRUD de base:
 * - save(entity)
 * - findById(id)
 * - findAll()
 * - deleteById(id)
 * - existsById(id)
 * - count()
 */
@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Integer> {

    // ============================================
    // MÉTHODES DÉRIVÉES (Query Methods)
    // Spring génère automatiquement l'implémentation
    // ============================================

    /**
     * Rechercher un médecin par email
     * Génère: SELECT * FROM medecin WHERE email = ?
     */
    Optional<Medecin> findByEmail(String email);

    /**
     * Vérifier si un email existe déjà
     * Génère: SELECT COUNT(*) > 0 FROM medecin WHERE email = ?
     */
    boolean existsByEmail(String email);

    /**
     * Rechercher des médecins par nom (contient)
     * Génère: SELECT * FROM medecin WHERE nom LIKE %?%
     */
    List<Medecin> findByNomContaining(String nom);

    /**
     * Rechercher des médecins par prénom (contient)
     * Génère: SELECT * FROM medecin WHERE prenom LIKE %?%
     */
    List<Medecin> findByPrenomContaining(String prenom);

    /**
     * Rechercher des médecins par nom ET prénom (contient)
     * Génère: SELECT * FROM medecin WHERE nom LIKE %?% AND prenom LIKE %?%
     */
    List<Medecin> findByNomContainingAndPrenomContaining(String nom, String prenom);

    /**
     * Rechercher des médecins par nom OU prénom (contient)
     * Génère: SELECT * FROM medecin WHERE nom LIKE %?% OR prenom LIKE %?%
     */
    List<Medecin> findByNomContainingOrPrenomContaining(String nom, String prenom);

    /**
     * Rechercher par email ignorant la casse
     * Génère: SELECT * FROM medecin WHERE LOWER(email) = LOWER(?)
     */
    Optional<Medecin> findByEmailIgnoreCase(String email);

    // ============================================
    // MÉTHODES AVEC @Query PERSONNALISÉES
    // ============================================

    /**
     * Rechercher des médecins avec leurs diagnostics (EAGER loading)
     * Optimisation: charge les diagnostics en une seule requête (évite N+1)
     */
    @Query("SELECT DISTINCT m FROM Medecin m LEFT JOIN FETCH m.diagnostics")
    List<Medecin> findAllWithDiagnostics();

    /**
     * Rechercher un médecin avec ses diagnostics par ID
     */
    @Query("SELECT m FROM Medecin m LEFT JOIN FETCH m.diagnostics WHERE m.id = :id")
    Optional<Medecin> findByIdWithDiagnostics(@Param("id") int id);

    /**
     * Compter le nombre de diagnostics d'un médecin
     */
    @Query("SELECT COUNT(d) FROM Diagnostic d WHERE d.medecin.id = :medecinId")
    long countDiagnosticsByMedecinId(@Param("medecinId") int medecinId);

    /**
     * Rechercher des médecins qui ont établi au moins un diagnostic
     */
    @Query("SELECT DISTINCT m FROM Medecin m WHERE SIZE(m.diagnostics) > 0")
    List<Medecin> findMedecinsWithDiagnostics();

    /**
     * Rechercher des médecins sans diagnostics
     */
    @Query("SELECT m FROM Medecin m WHERE SIZE(m.diagnostics) = 0")
    List<Medecin> findMedecinsSansDiagnostics();

    /**
     * Recherche avancée par nom, prénom ou email (case-insensitive)
     * Utile pour une barre de recherche générale
     */
    @Query("SELECT m FROM Medecin m WHERE " +
            "LOWER(m.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(m.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(m.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Medecin> searchMedecins(@Param("searchTerm") String searchTerm);

    /**
     * Rechercher des médecins avec un nombre minimum de diagnostics
     */
    @Query("SELECT m FROM Medecin m WHERE SIZE(m.diagnostics) >= :minCount")
    List<Medecin> findMedecinsWithMinDiagnostics(@Param("minCount") int minCount);

    /**
     * Rechercher les médecins créés par un administrateur spécifique
     */
    List<Medecin> findByAdministrateurId(Integer id);
}