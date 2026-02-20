package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.FeedbackIARequestDTO;
import com.polytechnique.backend.dto.response.FeedbackIAResponseDTO;

import java.util.List;

/**
 * Service pour gérer les feedbacks des médecins sur les prédictions IA
 */
public interface FeedbackIAService {

    FeedbackIAResponseDTO createFeedback(FeedbackIARequestDTO requestDTO);

    FeedbackIAResponseDTO getFeedbackById(int id);

    List<FeedbackIAResponseDTO> getAllFeedbacks();

    List<FeedbackIAResponseDTO> getFeedbacksByMedecin(int medecinId);

    void deleteFeedback(int id);

    /**
     * Génère un contenu CSV à partir de tous les feedbacks
     */
    String generateCsv();
}
