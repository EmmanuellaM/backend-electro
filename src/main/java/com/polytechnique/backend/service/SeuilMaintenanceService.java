package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.SeuilMaintenanceRequestDTO;
import com.polytechnique.backend.dto.response.SeuilMaintenanceResponseDTO;

/**
 * Service pour gérer les seuils de maintenance des dispositifs
 */
public interface SeuilMaintenanceService {

    /**
     * Récupérer les seuils actuels
     */
    SeuilMaintenanceResponseDTO getSeuils();

    /**
     * Mettre à jour les seuils
     */
    SeuilMaintenanceResponseDTO updateSeuils(SeuilMaintenanceRequestDTO requestDTO);

    /**
     * Réinitialiser les seuils aux valeurs par défaut
     */
    SeuilMaintenanceResponseDTO resetSeuils();

    /**
     * Vérifie si les paramètres dépassent les seuils et met à jour le statut du
     * dispositif si nécessaire
     */
    void checkMaintenance(com.polytechnique.backend.entity.Parametres parametres);
}
