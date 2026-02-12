package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.SeuilMaintenanceRequestDTO;
import com.polytechnique.backend.dto.response.SeuilMaintenanceResponseDTO;
import com.polytechnique.backend.service.SeuilMaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST pour gérer les seuils de maintenance
 * Base URL: /api/seuils-maintenance
 */
@RestController
@RequestMapping("/seuils-maintenance")
@RequiredArgsConstructor
@Tag(name = "Seuils de Maintenance", description = "Configuration des seuils d'alerte pour le passage automatique en statut Maintenance")
public class SeuilMaintenanceController {

    private final SeuilMaintenanceService seuilMaintenanceService;

    @GetMapping
    @Operation(summary = "Récupérer les seuils actuels", description = "Retourne la configuration actuelle des seuils de maintenance. Si aucun seuil n'a été configuré, les valeurs par défaut sont retournées.")
    public ResponseEntity<SeuilMaintenanceResponseDTO> getSeuils() {
        return ResponseEntity.ok(seuilMaintenanceService.getSeuils());
    }

    @PutMapping
    @Operation(summary = "Modifier les seuils", description = "Met à jour les seuils de maintenance. Réservé au Super Admin.")
    public ResponseEntity<SeuilMaintenanceResponseDTO> updateSeuils(
            @Valid @RequestBody SeuilMaintenanceRequestDTO requestDTO) {
        return ResponseEntity.ok(seuilMaintenanceService.updateSeuils(requestDTO));
    }

    @PostMapping("/reset")
    @Operation(summary = "Réinitialiser les seuils", description = "Remet les seuils à leurs valeurs par défaut.")
    public ResponseEntity<SeuilMaintenanceResponseDTO> resetSeuils() {
        return ResponseEntity.ok(seuilMaintenanceService.resetSeuils());
    }
}
