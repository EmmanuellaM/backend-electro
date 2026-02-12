package com.polytechnique.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Seuils de maintenance configurables pour les dispositifs")
public class SeuilMaintenanceResponseDTO {

    @Schema(description = "Température minimale acceptable (°C)", example = "35.5")
    private BigDecimal temperatureMin;

    @Schema(description = "Température maximale acceptable (°C)", example = "38.5")
    private BigDecimal temperatureMax;

    @Schema(description = "Fréquence fœtale minimale acceptable (bpm)", example = "110")
    private Integer frequenceFoetaleMin;

    @Schema(description = "Fréquence fœtale maximale acceptable (bpm)", example = "160")
    private Integer frequenceFoetaleMax;

    @Schema(description = "Pression systolique maximale acceptable (mmHg)", example = "140")
    private Integer pressionSystoliqueMax;

    @Schema(description = "Pression diastolique maximale acceptable (mmHg)", example = "90")
    private Integer pressionDiastoliqueMax;

    @Schema(description = "Glycémie maximale acceptable (mmol/L)", example = "7.0")
    private BigDecimal glycemieMax;

    @Schema(description = "Saturation en oxygène minimale acceptable (%)", example = "95")
    private Integer saturationOxygeneMin;

    @Schema(description = "Date de dernière mise à jour")
    private LocalDateTime updatedAt;
}
