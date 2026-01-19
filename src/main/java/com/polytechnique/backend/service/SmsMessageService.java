package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.SmsMessageRequestDTO;
import com.polytechnique.backend.dto.response.SmsMessageResponseDTO;

import java.util.List;

/**
 * Service pour gérer l'envoi et l'historique des SMS
 */
public interface SmsMessageService {

    /**
     * Envoyer un SMS à un infirmier (simulé pour le moment)
     */
    SmsMessageResponseDTO sendSms(SmsMessageRequestDTO requestDTO);

    /**
     * Récupérer l'historique des SMS envoyés à un infirmier
     */
    List<SmsMessageResponseDTO> getHistoryByInfirmier(int infirmierId);

    /**
     * Compter le nombre de SMS envoyés à un infirmier
     */
    long countByInfirmier(int infirmierId);
}
