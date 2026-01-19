package com.polytechnique.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Service client pour communiquer avec le service IA externe
 * Appelle l'API FastAPI pour obtenir des prédictions de diagnostic
 */
@Service
@Slf4j
public class AIClientService {

    private final WebClient webClient;

    @Value("${ai.service.url:http://localhost:5000}")
    private String aiServiceUrl;

    public AIClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Exception personnalisée pour signaler que le service IA n'est pas disponible
     */
    public static class AIServiceUnavailableException extends RuntimeException {
        public AIServiceUnavailableException(String message) {
            super(message);
        }
    }

    /**
     * Envoie les paramètres médicaux au service IA pour obtenir un diagnostic
     * 
     * @throws AIServiceUnavailableException si le service IA n'est pas accessible
     */
    public Map<String, Object> predictDiagnosis(
            int agePatient,
            double poids,
            double temperature,
            int systolique,
            int diastolique,
            int frequenceFoetale,
            double glycemie,
            boolean includeExplanation) {

        log.info("Appel au service IA pour diagnostic - Age: {}, Poids: {}", agePatient, poids);

        try {
            Map<String, Object> parametres = new HashMap<>();
            parametres.put("age_patient", agePatient);
            parametres.put("poids_patient", poids);
            parametres.put("temperature", temperature);
            parametres.put("pression_arterielle_systolique", systolique);
            parametres.put("pression_arterielle_diastolique", diastolique);
            parametres.put("frequence_foetale", frequenceFoetale);
            parametres.put("glycemie", glycemie);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("parametres", parametres);
            requestBody.put("include_explanation", includeExplanation);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.post()
                    .uri(aiServiceUrl + "/predict")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Réponse du service IA reçue: {}", response);
            return response;

        } catch (Exception e) {
            log.error("Erreur lors de l'appel au service IA: {}", e.getMessage());
            // NE PAS retourner de prédiction par défaut - lever une exception
            throw new AIServiceUnavailableException(
                    "Le service IA n'est pas disponible. Veuillez réessayer ultérieurement.");
        }
    }

    /**
     * Vérifie si le service IA est disponible
     */
    public boolean isAIServiceHealthy() {
        try {
            webClient.get()
                    .uri(aiServiceUrl + "/health")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return true;
        } catch (Exception e) {
            log.warn("Service IA non disponible: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Récupère les informations sur le modèle IA
     */
    public Map<String, Object> getModelInfo() {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.get()
                    .uri(aiServiceUrl + "/model/info")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return response;
        } catch (Exception e) {
            log.warn("Impossible de récupérer les infos du modèle: {}", e.getMessage());
            return Map.of(
                    "status", "unavailable",
                    "message", "Service IA non disponible");
        }
    }
}
