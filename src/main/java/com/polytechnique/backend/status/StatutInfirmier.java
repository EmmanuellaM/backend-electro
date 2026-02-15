package com.polytechnique.backend.status;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum représentant les différents statuts possibles d'un infirmier local
 */
@Schema(description = "Statut d'un infirmier local")
public enum StatutInfirmier {
    @Schema(description = "L'infirmier est actif et peut recevoir des alertes")
    ACTIF,

    @Schema(description = "L'infirmier est temporairement inactif")
    INACTIF,

    @Schema(description = "L'infirmier est supprimé (Soft Delete)")
    SUPPRIME
}
