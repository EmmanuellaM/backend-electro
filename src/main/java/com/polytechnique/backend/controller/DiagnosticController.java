package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.DiagnosticRequestDTO;
import com.polytechnique.backend.dto.response.DiagnosticResponseDTO;
import com.polytechnique.backend.service.DiagnosticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les diagnostics
 * Base URL: /api/diagnostics
 */
@RestController
@RequestMapping("/diagnostics")
@RequiredArgsConstructor
@Tag(name = "Diagnostics", description = "Gestion des diagnostics médicaux et notifications SMS automatiques")
public class DiagnosticController {

    private final DiagnosticService diagnosticService;

    @PostMapping
    @Operation(summary = "Créer un diagnostic", description = "Enregistre un diagnostic et envoie automatiquement un SMS à l'infirmier local.")
    public ResponseEntity<DiagnosticResponseDTO> createDiagnostic(
            @Valid @RequestBody DiagnosticRequestDTO requestDTO) {
        DiagnosticResponseDTO response = diagnosticService.createDiagnostic(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un diagnostic par ID", description = "Retourne les détails d'un diagnostic avec recommandations et niveau d'urgence.")
    public ResponseEntity<DiagnosticResponseDTO> getDiagnosticById(@PathVariable int id) {
        DiagnosticResponseDTO response = diagnosticService.getDiagnosticById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Lister tous les diagnostics", description = "Retourne la liste complète des diagnostics.")
    public ResponseEntity<List<DiagnosticResponseDTO>> getAllDiagnostics() {
        List<DiagnosticResponseDTO> response = diagnosticService.getAllDiagnostics();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un diagnostic", description = "Modifie le contenu, les recommandations ou le niveau d'urgence d'un diagnostic.")
    public ResponseEntity<DiagnosticResponseDTO> updateDiagnostic(
            @PathVariable int id,
            @Valid @RequestBody DiagnosticRequestDTO requestDTO) {
        DiagnosticResponseDTO response = diagnosticService.updateDiagnostic(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un diagnostic", description = "Supprime définitivement un diagnostic de la base de données.")
    public ResponseEntity<Void> deleteDiagnostic(@PathVariable int id) {
        diagnosticService.deleteDiagnostic(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medecin/{medecinId}")
    @Operation(summary = "Diagnostics par médecin", description = "Retourne tous les diagnostics établis par un médecin.")
    public ResponseEntity<List<DiagnosticResponseDTO>> getDiagnosticsByMedecin(
            @PathVariable int medecinId) {
        List<DiagnosticResponseDTO> response = diagnosticService.getDiagnosticsByMedecin(medecinId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/parametres/{parametresId}")
    @Operation(summary = "Diagnostic par paramètres patient", description = "Retourne les diagnostics associés à un ensemble de paramètres.")
    public ResponseEntity<List<DiagnosticResponseDTO>> getDiagnosticsByParametres(
            @PathVariable int parametresId) {
        List<DiagnosticResponseDTO> response = diagnosticService.getDiagnosticsByParametres(parametresId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/average-time")
    @Operation(summary = "Temps moyen de diagnostic", description = "Retourne le temps moyen (en secondes) entre la prise de mesures et la validation du diagnostic.")
    public ResponseEntity<Double> getAverageProcessingTime(@RequestParam(required = false) Integer adminId) {
        Double averageTime = diagnosticService.getAverageProcessingTime(adminId);
        return ResponseEntity.ok(averageTime != null ? averageTime : 0.0);
    }
}