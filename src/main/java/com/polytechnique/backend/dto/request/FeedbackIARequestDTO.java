package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour créer un feedback sur une prédiction IA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requête de feedback sur une prédiction IA")
public class FeedbackIARequestDTO {

    // === Paramètres utilisés ===

    @NotNull(message = "L'âge est obligatoire")
    @Schema(description = "Âge de la patiente", example = "28")
    private Integer agePatient;

    @NotNull(message = "Le poids est obligatoire")
    @Schema(description = "Poids en kg", example = "68.5")
    private Double poidsPatient;

    @Schema(description = "Taille en cm", example = "165.0")
    private Double taillePatient;

    @NotNull(message = "La température est obligatoire")
    @Schema(description = "Température en °C", example = "37.0")
    private Double temperature;

    @NotNull(message = "La pression systolique est obligatoire")
    @Schema(description = "Pression systolique en mmHg", example = "118")
    private Integer pressionSystolique;

    @NotNull(message = "La pression diastolique est obligatoire")
    @Schema(description = "Pression diastolique en mmHg", example = "75")
    private Integer pressionDiastolique;

    @NotNull(message = "La fréquence fœtale est obligatoire")
    @Schema(description = "Fréquence cardiaque fœtale en bpm", example = "145")
    private Integer frequenceFoetale;

    @NotNull(message = "La fréquence cardiaque maternelle est obligatoire")
    @Schema(description = "Fréquence cardiaque maternelle en bpm", example = "80")
    private Integer frequenceCardiaqueMere;

    @Schema(description = "Glycémie en mmol/L", example = "5.2")
    private Double glycemie;

    // === Résultat IA ===

    @NotNull(message = "La classe prédite est obligatoire")
    @Schema(description = "Classe prédite par l'IA", example = "Normal")
    private String classePredite;

    @NotNull(message = "Le score de confiance est obligatoire")
    @Schema(description = "Score de confiance (0-1)", example = "0.85")
    private Double scoreConfiance;

    @Schema(description = "Explication générée par l'IA")
    private String explicationMedecin;

    // === Feedback médecin ===

    @NotNull(message = "La note est obligatoire")
    @Min(value = 1, message = "La note minimum est 1")
    @Max(value = 5, message = "La note maximum est 5")
    @Schema(description = "Note du médecin (1-5)", example = "4")
    private Integer noteMedecin;

    @Schema(description = "Commentaire optionnel du médecin")
    private String commentaireMedecin;

    // === Références ===

    @NotNull(message = "L'ID du médecin est obligatoire")
    @Schema(description = "ID du médecin qui donne le feedback", example = "1")
    private Integer medecinId;

    @Schema(description = "ID des paramètres (optionnel)", example = "1")
    private Integer parametresId;
}
