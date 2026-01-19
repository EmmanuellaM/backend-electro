package com.polytechnique.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum représentant les différents statuts possibles d'un dispositif IoT
 */
@Schema(description = "Statut d'un dispositif IoT")
public enum StatutDispositif {

    @Schema(description = "Le dispositif est opérationnel et collecte des données")
    ACTIF,

    @Schema(description = "Le dispositif est temporairement désactivé")
    INACTIF,

    @Schema(description = "Le dispositif est en cours de maintenance ou réparation")
    MAINTENANCE,

    @Schema(description = "Le dispositif est créé mais en attente d'activation et de déploiement")
    EN_ATTENTE_ACTIVATION
}
