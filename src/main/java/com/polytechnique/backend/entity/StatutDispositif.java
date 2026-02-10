package com.polytechnique.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum représentant les différents statuts possibles d'un dispositif IoT
 */
@Schema(description = "Statut d'un dispositif IoT")
public enum StatutDispositif {

    @Schema(description = "Le dispositif est créé mais pas encore attribué à un administrateur")
    NON_ATTRIBUE,

    @Schema(description = "Le dispositif est attribué à un administrateur mais pas encore activé")
    EN_ATTENTE,

    @Schema(description = "Le dispositif est opérationnel et collecte des données")
    ACTIF,

    @Schema(description = "Le dispositif est temporairement désactivé")
    INACTIF,

    @Schema(description = "Le dispositif est en cours de maintenance ou réparation")
    MAINTENANCE
}
