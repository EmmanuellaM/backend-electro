package com.polytechnique.backend.status;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum représentant les différents statuts possibles d'un médecin
 */
@Schema(description = "Statut d'un médecin dans le système")
public enum StatutMedecin {

    @Schema(description = "Compte actif - le médecin peut se connecter et diagnostiquer")
    ACTIF,

    @Schema(description = "Compte désactivé définitivement - connexion impossible")
    INACTIF,

    @Schema(description = "Compte temporairement suspendu - connexion impossible")
    SUSPENDU
}
