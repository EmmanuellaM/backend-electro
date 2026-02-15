package com.polytechnique.backend.entity;

import com.polytechnique.backend.status.StatutParametre;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant les paramètres médicaux d'un patient
 * Correspond à la table "parametres" dans PostgreSQL
 */
@Entity
@Table(name = "parametres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parametres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametres")
    private Integer id;

    @NotBlank(message = "L'identifiant du patient est obligatoire")
    @Size(max = 50, message = "L'identifiant du patient ne peut pas dépasser 50 caractères")
    @Column(name = "identifiant_patient", nullable = false, length = 50)
    private String identifiantPatient;

    /**
     * Poids du patient en kilogrammes
     * NUMERIC(5,2) - CHECK: > 0 AND < 300
     */
    @NotNull(message = "Le poids du patient est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids doit être positif")
    @Column(name = "poids_patient", nullable = false, precision = 5, scale = 2)
    private BigDecimal poidsPatient;

    /**
     * Taille du patient en centimètres
     * NUMERIC(5,2) - CHECK: > 0 AND < 300
     */
    @NotNull(message = "La taille du patient est obligatoire")
    @DecimalMin(value = "1.0", message = "La taille doit être positive")
    @Column(name = "taille_patient", nullable = false, precision = 5, scale = 2)
    private BigDecimal taillePatient;

    /**
     * Âge du patient en années
     * CHECK: > 0 AND < 120
     */
    @NotNull(message = "L'âge du patient est obligatoire")
    @Min(value = 10, message = "L'âge doit être valide")
    @Max(value = 100, message = "L'âge doit être valide")
    @Column(name = "age_patient", nullable = true) // Nullable pour compatibilité mais devrait être not null
    private Integer agePatient;

    /**
     * Température du patient en degrés Celsius
     * NUMERIC(4,2) - CHECK: >= 30 AND <= 45
     */
    @NotNull(message = "La température est obligatoire")
    @DecimalMin(value = "30.0", message = "La température doit être >= 30°C")
    @Column(name = "temperature", nullable = false, precision = 4, scale = 2)
    private BigDecimal temperature;

    /**
     * Pression artérielle systolique en mmHg
     * CHECK: BETWEEN 40 AND 300
     */
    @Min(value = 40, message = "La pression systolique doit être >= 40")
    @Max(value = 300, message = "La pression systolique doit être <= 300")
    @Column(name = "pression_arterielle_systolique")
    private Integer pressionArterielleSystolique;

    /**
     * Pression artérielle diastolique en mmHg
     * CHECK: BETWEEN 20 AND 200
     */
    @Min(value = 20, message = "La pression diastolique doit être >= 20")
    @Max(value = 200, message = "La pression diastolique doit être <= 200")
    @Column(name = "pression_arterielle_diastolique")
    private Integer pressionArterielleDiastolique;

    /**
     * Fréquence cardiaque fœtale en battements par minute (bpm)
     * CHECK: BETWEEN 50 AND 220
     */
    @Min(value = 50, message = "La fréquence fœtale doit être >= 50")
    @Max(value = 220, message = "La fréquence fœtale doit être <= 220")
    @Column(name = "frequence_foetale")
    private Integer frequenceFoetale;

    /**
     * Glycémie (Blood Sugar) en mmol/L (ou mg/dL selon contexte, ici on stocke la
     * valeur brute)
     * NUMERIC(4,2)
     */
    @Column(name = "glycemie", precision = 4, scale = 2)
    private BigDecimal glycemie;

    /**
     * Saturation en oxygène (SpO2) en pourcentage
     * CHECK: BETWEEN 50 AND 100
     */
    @Min(value = 50, message = "La saturation en oxygène doit être >= 50%")
    @Max(value = 100, message = "La saturation en oxygène doit être <= 100%")
    @Column(name = "saturation_oxygene")
    private Integer saturationOxygene;

    /**
     * Date des dernières règles (DDR)
     * Utilisée pour calculer l'âge gestationnel
     */
    @Column(name = "date_dernieres_regles")
    private java.time.LocalDate dateDernieresRegles;

    /**
     * Date et heure de la mesure
     */
    @CreationTimestamp
    @Column(name = "date_mesure")
    private LocalDateTime dateMesure;

    /**
     * Statut des paramètres: en_attente, diagnostique, archive
     * Valeur par défaut: "en_attente"
     * Change automatiquement à "diagnostique" quand un diagnostic est créé
     * (trigger)
     */
    @Column(name = "statut", length = 20)
    private StatutParametre statut = StatutParametre.EN_ATTENTE;

    /**
     * ID du médecin qui a verrouillé ces paramètres pour consultation
     * Null si non verrouillé
     */
    @Column(name = "verrouille_par_medecin_id")
    private Integer verrouilleParMedecinId;

    /**
     * Date/heure du verrouillage
     * Utilisé pour auto-déverrouillage après timeout (30min)
     */
    @Column(name = "verrouille_at")
    private LocalDateTime verrouilleAt;

    /**
     * Relation Many-to-One optionnelle avec le médecin qui verrouille
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verrouille_par_medecin_id", insertable = false, updatable = false)
    private Medecin verrouilleParMedecin;

    /**
     * Date de création automatique
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Relation Many-to-One avec Dispositif
     * Plusieurs paramètres peuvent être collectés par un même dispositif
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dispositif", nullable = false)
    @NotNull(message = "Le dispositif est obligatoire")
    private Dispositif dispositif;

    /**
     * Relation One-to-Many avec Diagnostic
     * IMPORTANT: Un ensemble de paramètres peut avoir UN SEUL diagnostic
     * (contrainte UNIQUE)
     */
    @OneToMany(mappedBy = "parametres", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Diagnostic> diagnostics = new ArrayList<>();

    /**
     * Méthode utilitaire pour ajouter un diagnostic
     */
    public void addDiagnostic(Diagnostic diagnostic) {
        diagnostics.add(diagnostic);
        diagnostic.setParametres(this);
    }

    /**
     * Méthode utilitaire pour retirer un diagnostic
     */
    public void removeDiagnostic(Diagnostic diagnostic) {
        diagnostics.remove(diagnostic);
        diagnostic.setParametres(null);
    }
}