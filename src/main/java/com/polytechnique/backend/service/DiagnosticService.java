package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.DiagnosticRequestDTO;
import com.polytechnique.backend.dto.response.DiagnosticResponseDTO;

import java.util.List;

/**
 * Interface du service pour gérer les diagnostics
 */
public interface DiagnosticService {

    /**
     * Créer un nouveau diagnostic
     */
    DiagnosticResponseDTO createDiagnostic(DiagnosticRequestDTO requestDTO);

    /**
     * Récupérer un diagnostic par son ID
     */
    DiagnosticResponseDTO getDiagnosticById(int id);

    /**
     * Récupérer tous les diagnostics
     */
    List<DiagnosticResponseDTO> getAllDiagnostics();

    /**
     * Mettre à jour un diagnostic
     */
    DiagnosticResponseDTO updateDiagnostic(int id, DiagnosticRequestDTO requestDTO);

    /**
     * Supprimer un diagnostic
     */
    void deleteDiagnostic(int id);

    /**
     * Récupérer tous les diagnostics d'un médecin
     */
    List<DiagnosticResponseDTO> getDiagnosticsByMedecin(int medecinId);

    /**
     * Récupérer les diagnostics basés sur des paramètres spécifiques
     */
    List<DiagnosticResponseDTO> getDiagnosticsByParametres(int parametresId);

    void sendSmsDiagnostic(com.polytechnique.backend.dto.request.SmsDiagnosticRequest smsRequest);

    List<DiagnosticResponseDTO> getRecentDiagnostics();
}