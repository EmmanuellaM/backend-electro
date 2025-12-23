package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.Parametres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Parametres
 */
@Repository
public interface ParametresRepository extends JpaRepository<Parametres, Integer> {

       // ============================================
       // MÉTHODES DÉRIVÉES (Query Methods)
       // ============================================

       /**
        * Rechercher tous les paramètres d'un patient
        * Génère: SELECT * FROM parametres WHERE identifiant_patient = ?
        */
       List<Parametres> findByIdentifiantPatient(String identifiantPatient);

       /**
        * Rechercher les paramètres par dispositif
        * Génère: SELECT * FROM parametres WHERE id_dispositif = ?
        */
       List<Parametres> findByDispositifId(int dispositifId);

       /**
        * Rechercher les paramètres par dispositif triés par ID
        */
       List<Parametres> findByDispositifIdOrderByIdDesc(int dispositifId);

       /**
        * Rechercher les paramètres d'un patient triés par ID (plus récent en premier)
        */
       List<Parametres> findByIdentifiantPatientOrderByIdDesc(String identifiantPatient);

       /**
        * Vérifier si un patient a des paramètres
        */
       boolean existsByIdentifiantPatient(String identifiantPatient);

       /**
        * Compter les paramètres d'un patient
        */
       long countByIdentifiantPatient(String identifiantPatient);

       /**
        * Rechercher les paramètres avec un poids supérieur à une valeur
        */
       List<Parametres> findByPoidsPatientGreaterThan(BigDecimal poids);

       /**
        * Rechercher les paramètres avec un poids inférieur à une valeur
        */
       List<Parametres> findByPoidsPatientLessThan(BigDecimal poids);

       /**
        * Rechercher les paramètres avec une température supérieure à une valeur
        */
       List<Parametres> findByTemperatureGreaterThan(BigDecimal temperature);

       /**
        * Rechercher les paramètres avec une température inférieure à une valeur
        */
       List<Parametres> findByTemperatureLessThan(BigDecimal temperature);

       /**
        * Rechercher les paramètres avec poids et température dans une plage
        */
       List<Parametres> findByPoidsPatientBetweenAndTemperatureBetween(
                     BigDecimal poidsMin, BigDecimal poidsMax,
                     BigDecimal tempMin, BigDecimal tempMax);

       // ============================================
       // MÉTHODES AVEC @Query PERSONNALISÉES
       // ============================================

       /**
        * Rechercher des paramètres avec leur dispositif (EAGER loading)
        */
       @Query("SELECT p FROM Parametres p JOIN FETCH p.dispositif")
       List<Parametres> findAllWithDispositif();

       /**
        * Rechercher des paramètres avec dispositif et diagnostics
        */
       @Query("SELECT DISTINCT p FROM Parametres p " +
                     "LEFT JOIN FETCH p.dispositif " +
                     "LEFT JOIN FETCH p.diagnostics")
       List<Parametres> findAllWithDispositifAndDiagnostics();

       /**
        * Rechercher un paramètre avec son dispositif par ID
        */
       @Query("SELECT p FROM Parametres p JOIN FETCH p.dispositif WHERE p.id = :id")
       Optional<Parametres> findByIdWithDispositif(@Param("id") int id);

       /**
        * Rechercher les paramètres d'un patient avec diagnostics
        */
       @Query("SELECT DISTINCT p FROM Parametres p " +
                     "LEFT JOIN FETCH p.diagnostics " +
                     "WHERE p.identifiantPatient = :identifiantPatient")
       List<Parametres> findByIdentifiantPatientWithDiagnostics(@Param("identifiantPatient") String identifiantPatient);

       /**
        * Compter le nombre de diagnostics associés à un paramètre
        */
       @Query("SELECT COUNT(d) FROM Diagnostic d WHERE d.parametres.id = :parametresId")
       long countDiagnosticsByParametresId(@Param("parametresId") int parametresId);

       /**
        * Rechercher les paramètres ayant au moins un diagnostic
        */
       @Query("SELECT DISTINCT p FROM Parametres p WHERE SIZE(p.diagnostics) > 0")
       List<Parametres> findParametresWithDiagnostics();

       /**
        * Rechercher les paramètres sans diagnostics
        */
       @Query("SELECT p FROM Parametres p WHERE SIZE(p.diagnostics) = 0")
       List<Parametres> findParametresSansDiagnostics();

       /**
        * Rechercher les paramètres par plage de poids
        */
       @Query("SELECT p FROM Parametres p WHERE p.poidsPatient BETWEEN :minPoids AND :maxPoids")
       List<Parametres> findByPoidsRange(@Param("minPoids") BigDecimal minPoids,
                     @Param("maxPoids") BigDecimal maxPoids);

       /**
        * Rechercher les paramètres par plage de température
        */
       @Query("SELECT p FROM Parametres p WHERE p.temperature BETWEEN :minTemp AND :maxTemp")
       List<Parametres> findByTemperatureRange(@Param("minTemp") BigDecimal minTemp,
                     @Param("maxTemp") BigDecimal maxTemp);

       /**
        * Rechercher les paramètres avec température anormale (fièvre > 37.5)
        */
       @Query("SELECT p FROM Parametres p WHERE p.temperature > 37.5")
       List<Parametres> findParametresAvecFievre();

       /**
        * Rechercher les paramètres avec température basse (< 36.0)
        */
       @Query("SELECT p FROM Parametres p WHERE p.temperature < 36.0")
       List<Parametres> findParametresAvecHypothermie();

       /**
        * Rechercher les paramètres avec fréquence fœtale anormale
        * Normale: entre 110 et 160 bpm
        */
       @Query("SELECT p FROM Parametres p WHERE " +
                     "p.frequenceFoetale IS NOT NULL AND " +
                     "(p.frequenceFoetale < 110 OR p.frequenceFoetale > 160)")
       List<Parametres> findParametresAvecFrequenceFoetaleAnormale();

       /**
        * Statistiques: Poids moyen des patients
        */
       @Query("SELECT AVG(p.poidsPatient) FROM Parametres p")
       BigDecimal getAveragePoidsPatient();

       /**
        * Statistiques: Température moyenne
        */
       @Query("SELECT AVG(p.temperature) FROM Parametres p")
       BigDecimal getAverageTemperature();

       /**
        * Statistiques: Fréquence fœtale moyenne
        */
       @Query("SELECT AVG(p.frequenceFoetale) FROM Parametres p WHERE p.frequenceFoetale IS NOT NULL")
       Double getAverageFrequenceFoetale();

       /**
        * Rechercher les N derniers paramètres collectés
        */
       @Query("SELECT p FROM Parametres p ORDER BY p.id DESC")
       List<Parametres> findLatestParametres();

       /**
        * Rechercher les paramètres d'un patient dans un centre spécifique
        */
       @Query("SELECT p FROM Parametres p " +
                     "WHERE p.identifiantPatient = :identifiantPatient " +
                     "AND p.dispositif.id = :dispositifId")
       List<Parametres> findByPatientAndDispositif(@Param("identifiantPatient") String identifiantPatient,
                     @Param("dispositifId") int dispositifId);

       /**
        * Compter les patients uniques
        */
       @Query("SELECT COUNT(DISTINCT p.identifiantPatient) FROM Parametres p")
       long countDistinctPatients();

       /**
        * Rechercher des paramètres par statut
        */
       List<Parametres> findByStatut(String statut);
}