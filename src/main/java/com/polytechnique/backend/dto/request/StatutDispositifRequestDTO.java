package com.polytechnique.backend.dto.request;

import com.polytechnique.backend.entity.StatutDispositif;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO pour modifier uniquement le statut d'un dispositif
 */
@Data
@Schema(description = "Requête pour modifier le statut d'un dispositif")
public class StatutDispositifRequestDTO {

    @Schema(description = "Nouveau statut du dispositif. Valeurs possibles: ACTIF (opérationnel), INACTIF (désactivé), MAINTENANCE (en réparation)", example = "ACTIF", allowableValues = {
            "ACTIF", "INACTIF", "MAINTENANCE" }, requiredMode = Schema.RequiredMode.REQUIRED)
    private StatutDispositif statut;
}
