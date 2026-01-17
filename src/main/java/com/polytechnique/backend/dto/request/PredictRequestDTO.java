package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requête de prédiction IA pour le suivi de grossesse")
public class PredictRequestDTO {

    @NotNull(message = "L'âge est obligatoire")
    @Min(value = 12, message = "L'âge doit être supérieur ou égal à 12 ans")
    @Max(value = 60, message = "L'âge doit être inférieur ou égal à 60 ans")
    @Schema(description = "Âge de la patiente en années", example = "28")
    private Integer agePatient;

    @NotNull(message = "Le poids est obligatoire")
    @Min(value = 30, message = "Le poids doit être supérieur à 30 kg")
    @Schema(description = "Poids de la patiente en kg", example = "68.5")
    private Double poidsPatient;

    @NotNull(message = "La température est obligatoire")
    @Schema(description = "Température corporelle en °C", example = "37.0")
    private Double temperature;

    @NotNull(message = "La pression systolique est obligatoire")
    @Schema(description = "Pression artérielle systolique en mmHg", example = "118")
    private Integer pressionArterielleSystolique;

    @NotNull(message = "La pression diastolique est obligatoire")
    @Schema(description = "Pression artérielle diastolique en mmHg", example = "75")
    private Integer pressionArterielleDiastolique;

    @NotNull(message = "La fréquence cardiaque fœtale est obligatoire")
    @Schema(description = "Fréquence cardiaque fœtale en bpm", example = "145")
    private Integer frequenceFoetale;

    @NotNull(message = "La glycémie est obligatoire")
    @Schema(description = "Glycémie en mmol/L", example = "5.2")
    private Double glycemie;

    @Schema(description = "Inclure l'explication SHAP dans la réponse", example = "true")
    private Boolean includeExplanation = true;
}
