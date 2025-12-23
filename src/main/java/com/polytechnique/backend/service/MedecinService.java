package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.MedecinRequestDTO;
import com.polytechnique.backend.dto.response.MedecinResponseDTO;

import java.util.List;

/**
 * Interface du service pour gérer les médecins
 */
public interface MedecinService {

    /**
     * Créer un nouveau médecin
     */
    MedecinResponseDTO createMedecin(MedecinRequestDTO requestDTO);

    /**
     * Récupérer un médecin par son ID
     */
    MedecinResponseDTO getMedecinById(int id);

    /**
     * Récupérer tous les médecins
     */
    List<MedecinResponseDTO> getAllMedecins();

    /**
     * Mettre à jour un médecin
     */
    MedecinResponseDTO updateMedecin(int id, MedecinRequestDTO requestDTO);

    /**
     * Supprimer un médecin
     */
    void deleteMedecin(int id);

    /**
     * Récupérer un médecin par email
     */
    /**
     * Récupérer un médecin par email
     */
    MedecinResponseDTO getMedecinByEmail(String email);

    List<MedecinResponseDTO> getMedecinsByStatut(String statut);

    MedecinResponseDTO updateMedecinStatut(int id, String statut);

    List<MedecinResponseDTO> searchMedecins(String query);

    com.polytechnique.backend.dto.response.MedecinStatsDTO getMedecinStats(int id);
}