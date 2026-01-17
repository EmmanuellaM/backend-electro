package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant un diagnostic généré par l'IA
 * Correspond à la table "diagnostic_ia" dans PostgreSQL
 */
@Entity
@Table(name = "diagnostic_ia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diagnostic_ia")
    private Integer id;

    /**
     * Classe prédite par l'IA
     * Valeurs possibles: normal, pre_eclampsie, diabete_gestationnel,
     * infection, souffrance_foetale, travail_premature
     */
    @NotBlank(message = "La classe prédite est obligatoire")
    @Column(name = "classe_predite", nullable = false, length = 50)
    private String classePredite;

    /**
     * Score de confiance de la prédiction (0.0 à 1.0)
     */
    @NotNull(message = "Le score de confiance est obligatoire")
    @DecimalMin(value = "0.0", message = "Le score de confiance doit être >= 0")
    @DecimalMax(value = "1.0", message = "Le score de confiance doit être <= 1")
    @Column(name = "score_confiance", nullable = false)
    private Double scoreConfiance;

    /**
     * Explication SHAP au format JSON
     * Contient les paramètres influents et leurs poids
     */
    @Column(name = "explication_json", columnDefinition = "TEXT")
    private String explicationJson;

    /**
     * Recommandations cliniques au format JSON
     */
    @Column(name = "recommandations", columnDefinition = "TEXT")
    private String recommandations;

    /**
     * Probabilités pour toutes les classes au format JSON
     */
    @Column(name = "probabilites", columnDefinition = "TEXT")
    private String probabilites;

    /**
     * Validation par le médecin
     * null = en attente, true = validé, false = rejeté
     */
    @Column(name = "valide_par_medecin")
    private Boolean valideParMedecin;

    /**
     * Commentaire du médecin lors de la validation
     */
    @Column(name = "commentaire_medecin", columnDefinition = "TEXT")
    private String commentaireMedecin;

    /**
     * Note attribuée par le médecin à la qualité du diagnostic IA (1-5)
     * null = pas encore noté
     */
    @Min(value = 1, message = "La note doit être >= 1")
    @Max(value = 5, message = "La note doit être <= 5")
    @Column(name = "note_ia")
    private Integer noteIa;

    /**
     * Date et heure de création du diagnostic IA
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Date et heure de dernière mise à jour
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Relation Many-to-One avec Parametres
     * Un diagnostic IA est basé sur un ensemble de paramètres
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parametres", nullable = false)
    @NotNull(message = "Les paramètres sont obligatoires")
    private Parametres parametres;

    /**
     * Relation Many-to-One avec Medecin (validateur)
     * Le médecin qui a validé ou rejeté le diagnostic
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medecin_validateur")
    private Medecin medecinValidateur;
}
