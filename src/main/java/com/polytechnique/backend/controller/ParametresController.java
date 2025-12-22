package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.ParametresRequestDTO;
import com.polytechnique.backend.dto.response.ParametresResponseDTO;
import com.polytechnique.backend.service.ParametresService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les paramètres médicaux
 * Base URL: /api/parametres
 */
@RestController
@RequestMapping("/parametres")
@RequiredArgsConstructor
public class ParametresController {

    private final ParametresService parametresService;

    /**
     * Créer de nouveaux paramètres
     * POST /api/parametres
     */
    @PostMapping
    public ResponseEntity<ParametresResponseDTO> createParametres(
            @Valid @RequestBody ParametresRequestDTO requestDTO) {
        ParametresResponseDTO response = parametresService.createParametres(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Récupérer des paramètres par leur ID
     * GET /api/parametres/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParametresResponseDTO> getParametresById(@PathVariable int id) {
        ParametresResponseDTO response = parametresService.getParametresById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer tous les paramètres
     * GET /api/parametres
     */
    @GetMapping
    public ResponseEntity<List<ParametresResponseDTO>> getAllParametres() {
        List<ParametresResponseDTO> response = parametresService.getAllParametres();
        return ResponseEntity.ok(response);
    }

    /**
     * Mettre à jour des paramètres
     * PUT /api/parametres/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParametresResponseDTO> updateParametres(
            @PathVariable int id,
            @Valid @RequestBody ParametresRequestDTO requestDTO) {
        ParametresResponseDTO response = parametresService.updateParametres(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprimer des paramètres
     * DELETE /api/parametres/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParametres(@PathVariable int id) {
        parametresService.deleteParametres(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupérer tous les paramètres d'un patient
     * GET /api/parametres/patient/{identifiantPatient}
     */
    @GetMapping("/patient/{identifiantPatient}")
    public ResponseEntity<List<ParametresResponseDTO>> getParametresByPatient(
            @PathVariable String identifiantPatient) {
        List<ParametresResponseDTO> response = 
            parametresService.getParametresByPatient(identifiantPatient);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer les paramètres par dispositif
     * GET /api/parametres/dispositif/{dispositifId}
     */
    @GetMapping("/dispositif/{dispositifId}")
    public ResponseEntity<List<ParametresResponseDTO>> getParametresByDispositif(
            @PathVariable int dispositifId) {
        List<ParametresResponseDTO> response = 
            parametresService.getParametresByDispositif(dispositifId);
        return ResponseEntity.ok(response);
    }
}