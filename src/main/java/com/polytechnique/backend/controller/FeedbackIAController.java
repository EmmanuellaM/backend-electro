package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.FeedbackIARequestDTO;
import com.polytechnique.backend.dto.response.FeedbackIAResponseDTO;
import com.polytechnique.backend.service.FeedbackIAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les feedbacks sur les prédictions IA.
 * Ces données servent à améliorer le modèle de prédiction.
 */
@RestController
@RequestMapping("/feedback-ia")
@RequiredArgsConstructor
@Tag(name = "Feedback IA", description = "Gestion des feedbacks des médecins sur les prédictions IA pour l'amélioration du modèle")
public class FeedbackIAController {

    private final FeedbackIAService feedbackIAService;

    @PostMapping
    @Operation(summary = "Créer un feedback", description = "Enregistre le feedback d'un médecin sur une prédiction IA avec la note (1-5) et les paramètres utilisés")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Feedback créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
    })
    public ResponseEntity<FeedbackIAResponseDTO> createFeedback(@Valid @RequestBody FeedbackIARequestDTO requestDTO) {
        FeedbackIAResponseDTO response = feedbackIAService.createFeedback(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un feedback par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feedback trouvé"),
            @ApiResponse(responseCode = "404", description = "Feedback non trouvé")
    })
    public ResponseEntity<FeedbackIAResponseDTO> getFeedbackById(@PathVariable int id) {
        FeedbackIAResponseDTO response = feedbackIAService.getFeedbackById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Lister tous les feedbacks", description = "Retourne tous les feedbacks pour l'analyse et l'amélioration du modèle")
    public ResponseEntity<List<FeedbackIAResponseDTO>> getAllFeedbacks() {
        List<FeedbackIAResponseDTO> response = feedbackIAService.getAllFeedbacks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/medecin/{medecinId}")
    @Operation(summary = "Feedbacks par médecin", description = "Retourne les feedbacks donnés par un médecin spécifique")
    public ResponseEntity<List<FeedbackIAResponseDTO>> getFeedbacksByMedecin(@PathVariable int medecinId) {
        List<FeedbackIAResponseDTO> response = feedbackIAService.getFeedbacksByMedecin(medecinId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un feedback")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Feedback supprimé"),
            @ApiResponse(responseCode = "404", description = "Feedback non trouvé")
    })
    public ResponseEntity<Void> deleteFeedback(@PathVariable int id) {
        feedbackIAService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
