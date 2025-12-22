package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.MedecinRequestDTO;
import com.polytechnique.backend.dto.response.MedecinResponseDTO;
import com.polytechnique.backend.service.MedecinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les médecins
 * Base URL: /api/medecins
 */
@RestController
@RequestMapping("/medecins")
@RequiredArgsConstructor
public class MedecinController {

    private final MedecinService medecinService;

    /**
     * Créer un nouveau médecin
     * POST /api/medecins
     */
    @PostMapping
    public ResponseEntity<MedecinResponseDTO> createMedecin(
            @Valid @RequestBody MedecinRequestDTO requestDTO) {
        MedecinResponseDTO response = medecinService.createMedecin(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Récupérer un médecin par son ID
     * GET /api/medecins/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedecinResponseDTO> getMedecinById(@PathVariable int id) {
        MedecinResponseDTO response = medecinService.getMedecinById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer tous les médecins
     * GET /api/medecins
     */
    @GetMapping
    public ResponseEntity<List<MedecinResponseDTO>> getAllMedecins() {
        List<MedecinResponseDTO> response = medecinService.getAllMedecins();
        return ResponseEntity.ok(response);
    }

    /**
     * Mettre à jour un médecin
     * PUT /api/medecins/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedecinResponseDTO> updateMedecin(
            @PathVariable int id,
            @Valid @RequestBody MedecinRequestDTO requestDTO) {
        MedecinResponseDTO response = medecinService.updateMedecin(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprimer un médecin
     * DELETE /api/medecins/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable int id) {
        medecinService.deleteMedecin(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Rechercher un médecin par email
     * GET /api/medecins/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<MedecinResponseDTO> getMedecinByEmail(@PathVariable String email) {
        MedecinResponseDTO response = medecinService.getMedecinByEmail(email);
        return ResponseEntity.ok(response);
    }
}