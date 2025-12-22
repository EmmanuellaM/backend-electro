package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.DiagnosticRequestDTO;
import com.polytechnique.backend.dto.response.DiagnosticResponseDTO;
import com.polytechnique.backend.service.DiagnosticService;
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
public class DiagnosticController {

    private final DiagnosticService diagnosticService;

    /**
     * Créer un nouveau diagnostic
     * POST /api/diagnostics
     */
    @PostMapping
    public ResponseEntity<DiagnosticResponseDTO> createDiagnostic(
            @Valid @RequestBody DiagnosticRequestDTO requestDTO) {
        DiagnosticResponseDTO response = diagnosticService.createDiagnostic(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Récupérer un diagnostic par son ID
     * GET /api/diagnostics/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticResponseDTO> getDiagnosticById(@PathVariable int id) {
        DiagnosticResponseDTO response = diagnosticService.getDiagnosticById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer tous les diagnostics
     * GET /api/diagnostics
     */
    @GetMapping
    public ResponseEntity<List<DiagnosticResponseDTO>> getAllDiagnostics() {
        List<DiagnosticResponseDTO> response = diagnosticService.getAllDiagnostics();
        return ResponseEntity.ok(response);
    }

    /**
     * Mettre à jour un diagnostic
     * PUT /api/diagnostics/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticResponseDTO> updateDiagnostic(
            @PathVariable int id,
            @Valid @RequestBody DiagnosticRequestDTO requestDTO) {
        DiagnosticResponseDTO response = diagnosticService.updateDiagnostic(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprimer un diagnostic
     * DELETE /api/diagnostics/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiagnostic(@PathVariable int id) {
        diagnosticService.deleteDiagnostic(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupérer tous les diagnostics d'un médecin
     * GET /api/diagnostics/medecin/{medecinId}
     */
    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<DiagnosticResponseDTO>> getDiagnosticsByMedecin(
            @PathVariable int medecinId) {
        List<DiagnosticResponseDTO> response = 
            diagnosticService.getDiagnosticsByMedecin(medecinId);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer les diagnostics basés sur des paramètres spécifiques
     * GET /api/diagnostics/parametres/{parametresId}
     */
    @GetMapping("/parametres/{parametresId}")
    public ResponseEntity<List<DiagnosticResponseDTO>> getDiagnosticsByParametres(
            @PathVariable int parametresId) {
        List<DiagnosticResponseDTO> response = 
            diagnosticService.getDiagnosticsByParametres(parametresId);
        return ResponseEntity.ok(response);
    }
}