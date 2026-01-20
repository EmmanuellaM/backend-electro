package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.DispositifRequestDTO;
import com.polytechnique.backend.dto.request.ActivationDispositifRequestDTO;
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
     * Récupérer tous les dispositifs (filtrés par admin si adminId fourni)
     */
    List<DispositifResponseDTO> getAllDispositifs(Integer adminId);

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

    /**
     * Mettre à jour uniquement le statut d'un dispositif
     */
    DispositifResponseDTO updateStatut(int id, String statut);

    /**
     * Activer un dispositif en assignant un infirmier
     */
    DispositifResponseDTO activerDispositif(int id, ActivationDispositifRequestDTO requestDTO);
}