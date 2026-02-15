package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entité pour stocker les feedbacks des médecins sur les prédictions IA.
 * Ces données servent à améliorer le modèle de prédiction.
 */
@Entity
@Table(name = "feedback_ia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    private Integer id;

    // === Paramètres utilisés pour la prédiction ===

    @NotNull
    @Column(name = "age_patient")
    private Integer agePatient;

    @NotNull
    @Column(name = "poids_patient")
    private Double poidsPatient;

    @NotNull
    @Column(name = "taille_patient", nullable = false)
    private Double taillePatient;

    @NotNull
    @Column(name = "temperature")
    private Double temperature;

    @NotNull
    @Column(name = "pression_systolique")
    private Integer pressionSystolique;

    @NotNull
    @Column(name = "pression_diastolique")
    private Integer pressionDiastolique;

    @NotNull
    @Column(name = "frequence_foetale")
    private Integer frequenceFoetale;

    @Column(name = "glycemie")
    private Double glycemie;

    // === Résultat de la prédiction IA ===

    @NotNull
    @Column(name = "classe_predite", length = 100)
    private String classePredite;

    @NotNull
    @Column(name = "score_confiance")
    private Double scoreConfiance;

    @Column(name = "explication_medecin", columnDefinition = "TEXT")
    private String explicationMedecin;

    // === Feedback du médecin ===

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "note_medecin")
    private Integer noteMedecin;

    @Column(name = "commentaire_medecin", columnDefinition = "TEXT")
    private String commentaireMedecin;

    // === Relations ===

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medecin", nullable = false)
    private Medecin medecin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parametres")
    private Parametres parametres;

    // === Métadonnées ===

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
