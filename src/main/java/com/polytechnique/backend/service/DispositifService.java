package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.DispositifRequestDTO;
import com.polytechnique.backend.dto.response.DispositifResponseDTO;

import java.util.List;

/**
 * Interface du service pour gérer les dispositifs
 */
public interface DispositifService {

    /**
     * Créer un nouveau dispositif
     */
    DispositifResponseDTO createDispositif(DispositifRequestDTO requestDTO);

    /**
     * Récupérer un dispositif par son ID
     */
    DispositifResponseDTO getDispositifById(int id);

    /**
     * Récupérer tous les dispositifs
     */
    List<DispositifResponseDTO> getAllDispositifs();

    /**
     * Mettre à jour un dispositif
     */
    DispositifResponseDTO updateDispositif(int id, DispositifRequestDTO requestDTO);

    /**
     * Supprimer un dispositif
     */
    void deleteDispositif(int id);

    /**
     * Rechercher des dispositifs par nom de centre
     */
    List<DispositifResponseDTO> searchByNomCentre(String nomCentre);

    List<DispositifResponseDTO> getDispositifsByStatut(String statut);

    DispositifResponseDTO updateDispositifStatut(int id, String statut);

    com.polytechnique.backend.dto.response.DispositifStatsDTO getDispositifStats(int id);
}