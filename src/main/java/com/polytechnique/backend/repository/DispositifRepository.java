package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.Dispositif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Dispositif
 */
@Repository
public interface DispositifRepository extends JpaRepository<Dispositif, Integer> {

    // ============================================
    // MÉTHODES DÉRIVÉES (Query Methods)
    // ============================================

    /**
     * Rechercher des dispositifs par nom de centre (contient)
     * Génère: SELECT * FROM dispositif WHERE nom_centre_de_sante LIKE %?%
     */
    List<Dispositif> findByNomCentreDeSanteContaining(String nomCentre);

    /**
     * Rechercher un dispositif par son DevEUI (identifiant LoRaWAN unique)
     * Utilisé pour lier les messages uplink au dispositif
     */
    Optional<Dispositif> findByDeveui(String deveui);

    /**
     * Rechercher un dispositif par nom exact de centre
     * Génère: SELECT * FROM dispositif WHERE nom_centre_de_sante = ?
     */
    Optional<Dispositif> findByNomCentreDeSante(String nomCentreDeSante);

    /**
     * Rechercher des dispositifs par contact (contient)
     * Génère: SELECT * FROM dispositif WHERE contact LIKE %?%
     */
    List<Dispositif> findByContactContaining(String contact);

    /**
     * Rechercher par nom de centre ignorant la casse
     * Génère: SELECT * FROM dispositif WHERE LOWER(nom_centre_de_sante) LIKE
     * LOWER(%?%)
     */
    List<Dispositif> findByNomCentreDeSanteContainingIgnoreCase(String nomCentre);

    /**
     * Vérifier si un centre existe avec ce nom
     * Génère: SELECT COUNT(*) > 0 FROM dispositif WHERE nom_centre_de_sante = ?
     */
    boolean existsByNomCentreDeSante(String nomCentreDeSante);

    // ============================================
    // MÉTHODES AVEC @Query PERSONNALISÉES
    // ============================================

    /**
     * Rechercher des dispositifs avec leurs paramètres (EAGER loading)
     * Optimisation: charge les paramètres en une seule requête
     */
    @Query("SELECT DISTINCT d FROM Dispositif d LEFT JOIN FETCH d.parametres")
    List<Dispositif> findAllWithParametres();

    /**
     * Rechercher un dispositif avec ses paramètres par ID
     */
    @Query("SELECT d FROM Dispositif d LEFT JOIN FETCH d.parametres WHERE d.id = :id")
    Optional<Dispositif> findByIdWithParametres(@Param("id") int id);

    /**
     * Compter le nombre de paramètres collectés par un dispositif
     */
    @Query("SELECT COUNT(p) FROM Parametres p WHERE p.dispositif.id = :dispositifId")
    long countParametresByDispositifId(@Param("dispositifId") int dispositifId);

    /**
     * Rechercher des dispositifs qui ont collecté au moins un paramètre
     */
    @Query("SELECT DISTINCT d FROM Dispositif d WHERE SIZE(d.parametres) > 0")
    List<Dispositif> findDispositifsWithParametres();

    /**
     * Rechercher des dispositifs sans paramètres collectés
     */
    @Query("SELECT d FROM Dispositif d WHERE SIZE(d.parametres) = 0")
    List<Dispositif> findDispositifsSansParametres();

    /**
     * Recherche avancée par nom de centre ou contact (case-insensitive)
     */
    @Query("SELECT d FROM Dispositif d WHERE " +
            "LOWER(d.nomCentreDeSante) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(d.contact) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Dispositif> searchDispositifs(@Param("searchTerm") String searchTerm);

    /**
     * Rechercher des dispositifs avec un nombre minimum de paramètres collectés
     */
    @Query("SELECT d FROM Dispositif d WHERE SIZE(d.parametres) >= :minCount")
    List<Dispositif> findDispositifsWithMinParametres(@Param("minCount") int minCount);

    /**
     * Rechercher les dispositifs les plus actifs (avec le plus de paramètres)
     * Limite aux N premiers
     */
    @Query("SELECT d FROM Dispositif d ORDER BY SIZE(d.parametres) DESC")
    List<Dispositif> findTopActiveDispositifs();

    /**
     * Compter le nombre de centres de santé distincts
     */
    @Query("SELECT COUNT(DISTINCT d.nomCentreDeSante) FROM Dispositif d")
    long countDistinctCentresDeSante();

    /**
     * Rechercher les dispositifs créés par un administrateur spécifique
     */
    List<Dispositif> findByAdministrateurId(Integer id);
}