package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Diagnostic
 */
@Repository
public interface DiagnosticRepository extends JpaRepository<Diagnostic, Integer> {

       // ============================================
       // MÉTHODES DÉRIVÉES (Query Methods)
       // ============================================

       /**
        * Rechercher tous les diagnostics d'un médecin
        * Génère: SELECT * FROM diagnostic WHERE id_medecin = ?
        */
       List<Diagnostic> findByMedecinId(int medecinId);

       /**
        * Rechercher les diagnostics basés sur des paramètres spécifiques
        * Génère: SELECT * FROM diagnostic WHERE id_parametres = ?
        */
       List<Diagnostic> findByParametresId(int parametresId);

       /**
        * Rechercher les diagnostics d'un médecin triés par ID (plus récent en premier)
        */
       List<Diagnostic> findByMedecinIdOrderByIdDesc(int medecinId);

       /**
        * Calculer le temps moyen de traitement (en secondes)
        * Différence entre date_diagnostic et date_mesure
        */
       @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (d.date_diagnostic - p.date_mesure))) " +
                     "FROM diagnostic d " +
                     "JOIN parametres p ON d.id_parametres = p.id_parametres", nativeQuery = true)
       Double getAverageProcessingTime();

       /**
        * Rechercher les diagnostics par paramètres triés par ID
        */
       List<Diagnostic> findByParametresIdOrderByIdDesc(int parametresId);

       /**
        * Rechercher par contenu (contient un terme)
        */
       List<Diagnostic> findByContenuContaining(String terme);

       /**
        * Rechercher par contenu ignorant la casse
        */
       List<Diagnostic> findByContenuContainingIgnoreCase(String terme);

       /**
        * Compter les diagnostics d'un médecin
        */
       long countByMedecinId(int medecinId);

       /**
        * Compter les diagnostics basés sur des paramètres
        */
       long countByParametresId(int parametresId);

       /**
        * Compter les diagnostics créés après une date donnée (ex: dernières 24h)
        */
       long countByCreatedAtAfter(java.time.LocalDateTime date);

       /**
        * Compter les diagnostics créés entre deux dates
        */
       long countByCreatedAtBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

       /**
        * Vérifier si un médecin a établi au moins un diagnostic
        */
       boolean existsByMedecinId(int medecinId);

       // ============================================
       // MÉTHODES AVEC @Query PERSONNALISÉES
       // ============================================

       /**
        * Rechercher des diagnostics avec médecin et paramètres (EAGER loading)
        * Optimisation: charge tout en une seule requête
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN FETCH d.medecin " +
                     "JOIN FETCH d.parametres")
       List<Diagnostic> findAllWithMedecinAndParametres();

       /**
        * Rechercher un diagnostic avec médecin et paramètres par ID
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN FETCH d.medecin " +
                     "JOIN FETCH d.parametres " +
                     "WHERE d.id = :id")
       Optional<Diagnostic> findByIdWithMedecinAndParametres(@Param("id") int id);

       /**
        * Rechercher les diagnostics avec dispositif inclus
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN FETCH d.medecin " +
                     "JOIN FETCH d.parametres p " +
                     "JOIN FETCH p.dispositif")
       List<Diagnostic> findAllWithFullDetails();

       /**
        * Rechercher les diagnostics d'un médecin avec tous les détails
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN FETCH d.medecin m " +
                     "JOIN FETCH d.parametres p " +
                     "JOIN FETCH p.dispositif " +
                     "WHERE m.id = :medecinId")
       List<Diagnostic> findByMedecinIdWithFullDetails(@Param("medecinId") int medecinId);

       /**
        * Rechercher les diagnostics d'un patient spécifique
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN FETCH d.parametres p " +
                     "WHERE p.identifiantPatient = :identifiantPatient")
       List<Diagnostic> findByIdentifiantPatient(@Param("identifiantPatient") String identifiantPatient);

       /**
        * Rechercher les diagnostics d'un patient avec tous les détails
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN FETCH d.medecin " +
                     "JOIN FETCH d.parametres p " +
                     "WHERE p.identifiantPatient = :identifiantPatient " +
                     "ORDER BY d.id DESC")
       List<Diagnostic> findByIdentifiantPatientWithDetails(@Param("identifiantPatient") String identifiantPatient);

       /**
        * Rechercher les diagnostics par centre de santé
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN d.parametres p " +
                     "JOIN p.dispositif disp " +
                     "WHERE disp.id = :dispositifId")
       List<Diagnostic> findByDispositifId(@Param("dispositifId") int dispositifId);

       /**
        * Rechercher les diagnostics d'un médecin pour un patient spécifique
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN d.parametres p " +
                     "WHERE d.medecin.id = :medecinId " +
                     "AND p.identifiantPatient = :identifiantPatient")
       List<Diagnostic> findByMedecinAndPatient(@Param("medecinId") int medecinId,
                     @Param("identifiantPatient") String identifiantPatient);

       /**
        * Recherche de diagnostics par mot-clé dans le contenu (case-insensitive)
        */
       @Query("SELECT d FROM Diagnostic d WHERE LOWER(d.contenu) LIKE LOWER(CONCAT('%', :keyword, '%'))")
       List<Diagnostic> searchByKeyword(@Param("keyword") String keyword);

       /**
        * Recherche avancée dans diagnostics avec filtres multiples
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN d.medecin m " +
                     "JOIN d.parametres p " +
                     "WHERE (:medecinId IS NULL OR m.id = :medecinId) " +
                     "AND (:identifiantPatient IS NULL OR p.identifiantPatient = :identifiantPatient) " +
                     "AND (:keyword IS NULL OR LOWER(d.contenu) LIKE LOWER(CONCAT('%', :keyword, '%')))")
       List<Diagnostic> searchDiagnostics(@Param("medecinId") Integer medecinId,
                     @Param("identifiantPatient") String identifiantPatient,
                     @Param("keyword") String keyword);

       /**
        * Compter le nombre total de diagnostics par médecin
        */
       @Query("SELECT m.id, COUNT(d) FROM Diagnostic d " +
                     "JOIN d.medecin m " +
                     "GROUP BY m.id")
       List<Object[]> countDiagnosticsPerMedecin();

       /**
        * Compter le nombre de diagnostics par patient
        */
       @Query("SELECT p.identifiantPatient, COUNT(d) FROM Diagnostic d " +
                     "JOIN d.parametres p " +
                     "GROUP BY p.identifiantPatient")
       List<Object[]> countDiagnosticsPerPatient();

       /**
        * Rechercher les N derniers diagnostics
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN FETCH d.medecin " +
                     "JOIN FETCH d.parametres " +
                     "ORDER BY d.id DESC")
       List<Diagnostic> findLatestDiagnostics();

       /**
        * Rechercher les diagnostics avec paramètres anormaux (température > 37.5)
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN FETCH d.parametres p " +
                     "WHERE p.temperature > 37.5")
       List<Diagnostic> findDiagnosticsWithFievre();

       /**
        * Rechercher les diagnostics avec fréquence fœtale anormale
        */
       @Query("SELECT d FROM Diagnostic d " +
                     "JOIN FETCH d.parametres p " +
                     "WHERE p.frequenceFoetale IS NOT NULL " +
                     "AND (p.frequenceFoetale < 110 OR p.frequenceFoetale > 160)")
       List<Diagnostic> findDiagnosticsWithFrequenceAnormale();

       /**
        * Statistiques: Nombre de diagnostics par centre de santé
        */
       @Query("SELECT disp.nomCentreDeSante, COUNT(d) FROM Diagnostic d " +
                     "JOIN d.parametres p " +
                     "JOIN p.dispositif disp " +
                     "GROUP BY disp.nomCentreDeSante")
       List<Object[]> countDiagnosticsPerCentre();

       /**
        * Trouver les médecins les plus actifs (avec le plus de diagnostics)
        */
       @Query("SELECT m.id, m.nom, m.prenom, COUNT(d) FROM Diagnostic d " +
                     "JOIN d.medecin m " +
                     "GROUP BY m.id, m.nom, m.prenom " +
                     "ORDER BY COUNT(d) DESC")
       List<Object[]> findTopActiveMedecins();
}