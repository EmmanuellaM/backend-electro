package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant un diagnostic médical établi par un médecin
 * Correspond à la table "diagnostic" dans PostgreSQL
 * 
 * IMPORTANT: Un seul diagnostic par paramètre (contrainte UNIQUE sur
 * ID_Parametres)
 */
@Entity
@Table(name = "diagnostic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnostic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diagnostic")
    private Integer id;

    /**
     * Contenu du diagnostic médical
     * Type TEXT en base de données
     */
    @NotBlank(message = "Le contenu du diagnostic est obligatoire")
    @Column(name = "contenu", nullable = false, columnDefinition = "TEXT")
    private String contenu;

    /**
     * Date et heure du diagnostic
     */
    @Column(name = "date_diagnostic")
    private LocalDateTime dateDiagnostic;

    /**
     * Date de validation par le médecin
     */
    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    /**
     * Recommandations cliniques
     */
    @Column(name = "recommandations", columnDefinition = "TEXT")
    private String recommandations;

    /**
     * Niveau d'urgence (NORMAL, CRITIQUE)
     */
    @Column(name = "niveau_urgence", length = 20)
    private String niveauUrgence;

    /**
     * Date de création automatique
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Date de dernière mise à jour automatique (trigger)
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Relation Many-to-One avec Medecin
     * Plusieurs diagnostics peuvent être établis par un même médecin
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medecin", nullable = false)
    @NotNull(message = "Le médecin est obligatoire")
    private Medecin medecin;

    /**
     * Relation Many-to-One avec Parametres
     * IMPORTANT: UN SEUL diagnostic par paramètre (contrainte UNIQUE en BD)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parametres", nullable = false, unique = true)
    @NotNull(message = "Les paramètres sont obligatoires")
    private Parametres parametres;
}