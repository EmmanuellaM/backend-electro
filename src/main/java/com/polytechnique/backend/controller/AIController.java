package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.PredictRequestDTO;
import com.polytechnique.backend.dto.response.PredictResponseDTO;
import com.polytechnique.backend.service.AIClientService;
import com.polytechnique.backend.service.AIClientService.AIServiceUnavailableException;
import com.polytechnique.backend.service.AIExplanationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "IA & Diagnostic", description = "Services d'intelligence artificielle pour l'aide au diagnostic")
public class AIController {

    private final AIClientService aiClientService;
    private final AIExplanationService aiExplanationService;

    @PostMapping("/predict")
    @Operation(summary = "Lancer une analyse IA", description = "Envoie les constantes vitales au modèle IA et retourne un diagnostic avec explications.")
    public ResponseEntity<?> predictDiagnosis(@Valid @RequestBody PredictRequestDTO request) {

        try {
            // 1. Appel au service IA (Python)
            Map<String, Object> rawResponse = aiClientService.predictDiagnosis(
                    request.getAgePatient(),
                    request.getPoidsPatient(),
                    request.getTemperature(),
                    request.getPressionArterielleSystolique(),
                    request.getPressionArterielleDiastolique(),
                    request.getFrequenceFoetale(),
                    request.getGlycemie(),
                    request.getIncludeExplanation() != null ? request.getIncludeExplanation() : true);

            // 2. Construction de la réponse DTO
            PredictResponseDTO response = new PredictResponseDTO();

            response.setClassePredite((String) rawResponse.get("classe_predite"));
            response.setScoreConfiance((Double) rawResponse.get("score_confiance"));

            @SuppressWarnings("unchecked")
            Map<String, Double> probs = (Map<String, Double>) rawResponse.get("probabilites");
            response.setProbabilites(probs);

            // 3. Génération du message formaté pour le médecin
            String explanation = aiExplanationService.formatDoctorExplanation(rawResponse);
            response.setExplicationMedecin(explanation);

            // 4. Inclusion des données brutes (SHAP) pour les graphiques frontend
            @SuppressWarnings("unchecked")
            Map<String, Object> shap = (Map<String, Object>) rawResponse.get("explication");
            response.setExplicationSHAP(shap);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> recos = (List<Map<String, Object>>) rawResponse.get("recommandations");
            response.setRecommandationsIA(recos);

            return ResponseEntity.ok(response);

        } catch (AIServiceUnavailableException e) {
            // Service IA non disponible - retourner erreur HTTP 503
            log.warn("Service IA non disponible: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "error", "SERVICE_IA_INDISPONIBLE",
                            "message",
                            "Le service IA n'est pas disponible actuellement. Le diagnostic IA sera disponible ultérieurement.",
                            "serviceUnavailable", true));
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Vérifier l'état de l'IA", description = "Vérifie si le microservice IA est accessible.")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        boolean isUp = aiClientService.isAIServiceHealthy();
        return ResponseEntity.ok(Map.of(
                "status", isUp ? "UP" : "DOWN",
                "service", "AI Module"));
    }

    @GetMapping("/model-info")
    @Operation(summary = "Infos modèles", description = "Renvoie les métadonnées du modèle chargé.")
    public ResponseEntity<Map<String, Object>> getModelInfo() {
        return ResponseEntity.ok(aiClientService.getModelInfo());
    }
}
