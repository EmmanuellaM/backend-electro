package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.DiagnosticIARequestDTO;
import com.polytechnique.backend.dto.request.DiagnosticIAValidationDTO;
import com.polytechnique.backend.dto.response.DiagnosticIAResponseDTO;
import com.polytechnique.backend.service.DiagnosticIAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST pour les diagnostics générés par l'IA
 */
@RestController
@RequestMapping("/diagnostics-ia")
@RequiredArgsConstructor
@Tag(name = "Diagnostic IA", description = "Endpoints pour la gestion et la validation des diagnostics IA")
public class DiagnosticIAController {

    private final DiagnosticIAService diagnosticIAService;

    @PostMapping
    @Operation(summary = "Lancer un diagnostic IA", description = "Vérifie les contre-indications et appelle le service IA pour générer un diagnostic.")
    public ResponseEntity<DiagnosticIAResponseDTO> createDiagnosticIA(
            @Valid @RequestBody DiagnosticIARequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(diagnosticIAService.createDiagnosticIA(requestDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un diagnostic IA par ID")
    public ResponseEntity<DiagnosticIAResponseDTO> getDiagnosticIAById(@PathVariable Integer id) {
        return ResponseEntity.ok(diagnosticIAService.getDiagnosticIAById(id));
    }

    @GetMapping
    @Operation(summary = "Lister tous les diagnostics IA")
    public ResponseEntity<List<DiagnosticIAResponseDTO>> getAllDiagnosticsIA() {
        return ResponseEntity.ok(diagnosticIAService.getAllDiagnosticsIA());
    }

    @GetMapping("/patient/{identifiantPatient}/history")
    @Operation(summary = "Historique IA d'un patient")
    public ResponseEntity<List<DiagnosticIAResponseDTO>> getPatientHistory(@PathVariable String identifiantPatient) {
        return ResponseEntity.ok(diagnosticIAService.getPatientHistory(identifiantPatient));
    }

    @PutMapping("/{id}/validate")
    @Operation(summary = "Valider ou rejeter un diagnostic IA")
    public ResponseEntity<DiagnosticIAResponseDTO> validateDiagnostic(@PathVariable Integer id,
            @Valid @RequestBody DiagnosticIAValidationDTO validationDTO) {
        return ResponseEntity.ok(diagnosticIAService.validateDiagnostic(id, validationDTO));
    }

    @PutMapping("/{id}/rate")
    @Operation(summary = "Noter la qualité d'un diagnostic IA")
    public ResponseEntity<DiagnosticIAResponseDTO> rateDiagnostic(@PathVariable Integer id,
            @RequestBody Map<String, Object> payload) {
        Integer note = (Integer) payload.get("noteIa");
        String commentaire = (String) payload.get("commentaireMedecin");
        Integer medecinId = (Integer) payload.get("medecinId");

        return ResponseEntity.ok(diagnosticIAService.rateDiagnostic(id, note, commentaire, medecinId));
    }
}
