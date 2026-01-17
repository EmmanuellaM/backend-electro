package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.ParametresRequestDTO;
import com.polytechnique.backend.dto.response.ParametresResponseDTO;

import java.util.List;

/**
 * Interface du service pour gérer les paramètres médicaux
 */
public interface ParametresService {

    /**
     * Créer de nouveaux paramètres
     */
    ParametresResponseDTO createParametres(ParametresRequestDTO requestDTO);

    /**
     * Récupérer des paramètres par leur ID
     */
    ParametresResponseDTO getParametresById(int id);

    /**
     * Récupérer tous les paramètres
     */
    List<ParametresResponseDTO> getAllParametres();

    /**
     * Mettre à jour des paramètres
     */
    ParametresResponseDTO updateParametres(int id, ParametresRequestDTO requestDTO);

    /**
     * Supprimer des paramètres
     */
    void deleteParametres(int id);

    /**
     * Récupérer tous les paramètres d'un patient
     */
    List<ParametresResponseDTO> getParametresByPatient(String identifiantPatient);

    /**
     * Récupérer les paramètres par dispositif
     */
    List<ParametresResponseDTO> getParametresByDispositif(int dispositifId);

    /**
     * Verrouiller des paramètres pour un médecin (empêche l'accès concurrent)
     */
    ParametresResponseDTO lockParametres(int id, int medecinId);

    /**
     * Déverrouiller des paramètres
     */
    ParametresResponseDTO unlockParametres(int id);

    /**
     * Vérifier le statut de verrouillage
     */
    boolean isLocked(int id, int medecinId);
}