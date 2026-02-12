package com.polytechnique.backend.status;

/**
 * Statut d'un SMS envoyé
 */
public enum SmsStatus {
    SIMULATED, // SMS simulé (pas encore d'API configurée)
    PENDING, // En attente d'envoi
    SENT, // Envoyé avec succès
    FAILED // Échec d'envoi
}
