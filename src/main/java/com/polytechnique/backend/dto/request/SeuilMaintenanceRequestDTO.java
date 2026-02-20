package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requête de mise à jour des seuils de maintenance")
public class SeuilMaintenanceRequestDTO {

    @NotNull(message = "La température minimale est obligatoire")
    @DecimalMin(value = "30.0", message = "La température minimale doit être >= 30°C")
    @Schema(description = "Température minimale acceptable (°C)", example = "35.5")
    private BigDecimal temperatureMin;

    @NotNull(message = "La température maximale est obligatoire")
    @DecimalMin(value = "30.0", message = "La température maximale doit être >= 30°C")
    @Schema(description = "Température maximale acceptable (°C)", example = "38.5")
    private BigDecimal temperatureMax;

    @NotNull(message = "La fréquence fœtale minimale est obligatoire")
    @Min(value = 50, message = "La fréquence fœtale minimale doit être >= 50 bpm")
    @Schema(description = "Fréquence fœtale minimale acceptable (bpm)", example = "110")
    private Integer frequenceFoetaleMin;

    @NotNull(message = "La fréquence fœtale maximale est obligatoire")
    @Min(value = 50, message = "La fréquence fœtale maximale doit être >= 50 bpm")
    @Schema(description = "Fréquence fœtale maximale acceptable (bpm)", example = "160")
    private Integer frequenceFoetaleMax;

    @NotNull(message = "La fréquence cardiaque maternelle minimale est obligatoire")
    @Min(value = 40, message = "La fréquence cardiaque maternelle minimale doit être >= 40 bpm")
    @Schema(description = "Fréquence cardiaque maternelle minimale acceptable (bpm)", example = "60")
    private Integer frequenceCardiaqueMereMin;

    @NotNull(message = "La fréquence cardiaque maternelle maximale est obligatoire")
    @Min(value = 40, message = "La fréquence cardiaque maternelle maximale doit être >= 40 bpm")
    @Schema(description = "Fréquence cardiaque maternelle maximale acceptable (bpm)", example = "100")
    private Integer frequenceCardiaqueMereMax;

    @NotNull(message = "La pression systolique maximale est obligatoire")
    @Min(value = 40, message = "La pression systolique maximale doit être >= 40 mmHg")
    @Schema(description = "Pression systolique maximale acceptable (mmHg)", example = "140")
    private Integer pressionSystoliqueMax;

    @NotNull(message = "La pression diastolique maximale est obligatoire")
    @Min(value = 20, message = "La pression diastolique maximale doit être >= 20 mmHg")
    @Schema(description = "Pression diastolique maximale acceptable (mmHg)", example = "90")
    private Integer pressionDiastoliqueMax;

    @NotNull(message = "La glycémie maximale est obligatoire")
    @DecimalMin(value = "1.0", message = "La glycémie maximale doit être >= 1.0 mmol/L")
    @Schema(description = "Glycémie maximale acceptable (mmol/L)", example = "7.0")
    private BigDecimal glycemieMax;

    @NotNull(message = "La saturation en oxygène minimale est obligatoire")
    @Min(value = 50, message = "La saturation en oxygène minimale doit être >= 50%")
    @Schema(description = "Saturation en oxygène minimale acceptable (%)", example = "95")
    private Integer saturationOxygeneMin;
}
