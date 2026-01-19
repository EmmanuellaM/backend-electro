package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.MedecinRequestDTO;
import com.polytechnique.backend.dto.response.MedecinResponseDTO;
import com.polytechnique.backend.service.MedecinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Médecins", description = "Gestion des médecins et de leurs diagnostics")
public class MedecinController {

    private final MedecinService medecinService;

    @PostMapping
    @Operation(summary = "Créer un médecin", description = "Enregistre un nouveau médecin avec ses informations professionnelles et sa CNI.")
    public ResponseEntity<MedecinResponseDTO> createMedecin(
            @Valid @RequestBody MedecinRequestDTO requestDTO) {
        MedecinResponseDTO response = medecinService.createMedecin(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un médecin par ID", description = "Retourne les détails complets d'un médecin.")
    public ResponseEntity<MedecinResponseDTO> getMedecinById(@PathVariable int id) {
        MedecinResponseDTO response = medecinService.getMedecinById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Lister tous les médecins", description = "Retourne la liste complète des médecins enregistrés.")
    public ResponseEntity<List<MedecinResponseDTO>> getAllMedecins() {
        List<MedecinResponseDTO> response = medecinService.getAllMedecins();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un médecin", description = "Modifie les informations d'un médecin existant.")
    public ResponseEntity<MedecinResponseDTO> updateMedecin(
            @PathVariable int id,
            @Valid @RequestBody MedecinRequestDTO requestDTO) {
        MedecinResponseDTO response = medecinService.updateMedecin(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un médecin", description = "Supprime définitivement un médecin de la base de données.")
    public ResponseEntity<Void> deleteMedecin(@PathVariable int id) {
        medecinService.deleteMedecin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Rechercher par email", description = "Trouve un médecin par son adresse email.")
    public ResponseEntity<MedecinResponseDTO> getMedecinByEmail(@PathVariable String email) {
        MedecinResponseDTO response = medecinService.getMedecinByEmail(email);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Changer le mot de passe", description = "Permet à un médecin de changer son mot de passe.")
    public ResponseEntity<Void> changePassword(
            @PathVariable int id,
            @Valid @RequestBody com.polytechnique.backend.dto.request.ChangePasswordRequestDTO requestDTO) {
        medecinService.updatePassword(id, requestDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Modifier le statut d'un médecin", description = "Change uniquement le statut d'un médecin. Valeurs possibles: ACTIF, INACTIF.")
    public ResponseEntity<MedecinResponseDTO> updateStatut(
            @PathVariable int id,
            @RequestBody java.util.Map<String, String> body) {
        String newStatut = body.get("statut");
        MedecinResponseDTO response = medecinService.updateStatut(id, newStatut);
        return ResponseEntity.ok(response);
    }
}