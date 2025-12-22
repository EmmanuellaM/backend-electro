package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.DispositifRequestDTO;
import com.polytechnique.backend.dto.response.DispositifResponseDTO;
import com.polytechnique.backend.service.DispositifService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les dispositifs
 * Base URL: /api/dispositifs
 */
@RestController
@RequestMapping("/dispositifs")
@RequiredArgsConstructor
public class DispositifController {

    private final DispositifService dispositifService;

    /**
     * Créer un nouveau dispositif
     * POST /api/dispositifs
     */
    @PostMapping
    public ResponseEntity<DispositifResponseDTO> createDispositif(
            @Valid @RequestBody DispositifRequestDTO requestDTO) {
        DispositifResponseDTO response = dispositifService.createDispositif(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Récupérer un dispositif par son ID
     * GET /api/dispositifs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DispositifResponseDTO> getDispositifById(@PathVariable int id) {
        DispositifResponseDTO response = dispositifService.getDispositifById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer tous les dispositifs
     * GET /api/dispositifs
     */
    @GetMapping
    public ResponseEntity<List<DispositifResponseDTO>> getAllDispositifs() {
        List<DispositifResponseDTO> response = dispositifService.getAllDispositifs();
        return ResponseEntity.ok(response);
    }

    /**
     * Mettre à jour un dispositif
     * PUT /api/dispositifs/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<DispositifResponseDTO> updateDispositif(
            @PathVariable int id,
            @Valid @RequestBody DispositifRequestDTO requestDTO) {
        DispositifResponseDTO response = dispositifService.updateDispositif(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprimer un dispositif
     * DELETE /api/dispositifs/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispositif(@PathVariable int id) {
        dispositifService.deleteDispositif(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Rechercher des dispositifs par nom de centre
     * GET /api/dispositifs/search?nomCentre=Yaoundé
     */
    @GetMapping("/search")
    public ResponseEntity<List<DispositifResponseDTO>> searchByNomCentre(
            @RequestParam String nomCentre) {
        List<DispositifResponseDTO> response = dispositifService.searchByNomCentre(nomCentre);
        return ResponseEntity.ok(response);
    }
}