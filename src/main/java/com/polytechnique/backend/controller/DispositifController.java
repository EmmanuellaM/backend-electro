package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.DispositifRequestDTO;
import com.polytechnique.backend.dto.response.DispositifResponseDTO;
import com.polytechnique.backend.service.DispositifService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

/**
 * Controller REST pour gérer les dispositifs
 * Base URL: /api/dispositifs
 */
@RestController
@RequestMapping("/dispositifs")
@RequiredArgsConstructor
@Tag(name = "Dispositif", description = "API de gestion des dispositifs")
public class DispositifController {

        private final DispositifService dispositifService;

        /**
         * Créer un nouveau dispositif
         * POST /api/dispositifs
         */
        @Operation(summary = "Créer un nouveau dispositif", description = "Enregistre un nouveau dispositif dans le système.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Dispositif créé avec succès"),
                        @ApiResponse(responseCode = "400", description = "Données invalides")
        })
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
        @Operation(summary = "Récupérer un dispositif par ID", description = "Retourne les détails d'un dispositif spécifique.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Dispositif trouvé"),
                        @ApiResponse(responseCode = "404", description = "Dispositif non trouvé")
        })
        @GetMapping("/{id}")
        public ResponseEntity<DispositifResponseDTO> getDispositifById(
                        @Parameter(description = "ID du dispositif") @PathVariable int id) {
                DispositifResponseDTO response = dispositifService.getDispositifById(id);
                return ResponseEntity.ok(response);
        }

        /**
         * Récupérer tous les dispositifs
         * GET /api/dispositifs
         */
        @Operation(summary = "Récupérer tous les dispositifs", description = "Retourne la liste complète des dispositifs.")
        @ApiResponse(responseCode = "200", description = "Liste des dispositifs récupérée")
        @GetMapping
        public ResponseEntity<List<DispositifResponseDTO>> getAllDispositifs() {
                List<DispositifResponseDTO> response = dispositifService.getAllDispositifs();
                return ResponseEntity.ok(response);
        }

        /**
         * Mettre à jour un dispositif
         * PUT /api/dispositifs/{id}
         */
        @Operation(summary = "Mettre à jour un dispositif", description = "Met à jour les informations d'un dispositif existant.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Dispositif mis à jour avec succès"),
                        @ApiResponse(responseCode = "404", description = "Dispositif non trouvé"),
                        @ApiResponse(responseCode = "400", description = "Données invalides")
        })
        @PutMapping("/{id}")
        public ResponseEntity<DispositifResponseDTO> updateDispositif(
                        @Parameter(description = "ID du dispositif à mettre à jour") @PathVariable int id,
                        @Valid @RequestBody DispositifRequestDTO requestDTO) {
                DispositifResponseDTO response = dispositifService.updateDispositif(id, requestDTO);
                return ResponseEntity.ok(response);
        }

        /**
         * Supprimer un dispositif
         * DELETE /api/dispositifs/{id}
         */
        @Operation(summary = "Supprimer un dispositif", description = "Supprime un dispositif du système.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Dispositif supprimé avec succès"),
                        @ApiResponse(responseCode = "404", description = "Dispositif non trouvé")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDispositif(
                        @Parameter(description = "ID du dispositif à supprimer") @PathVariable int id) {
                dispositifService.deleteDispositif(id);
                return ResponseEntity.noContent().build();
        }

        /**
         * Rechercher des dispositifs par nom de centre
         * GET /api/dispositifs/search?nomCentre=Yaoundé
         */
        @Operation(summary = "Rechercher des dispositifs par centre", description = "Recherche les dispositifs situés dans un centre spécifique.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Résultats de la recherche"),
                        @ApiResponse(responseCode = "400", description = "Paramètre manquant ou invalide")
        })
        @GetMapping("/search")
        public ResponseEntity<List<DispositifResponseDTO>> searchByNomCentre(
                        @Parameter(description = "Nom du centre (ex: Yaoundé)") @RequestParam String nomCentre) {
                List<DispositifResponseDTO> response = dispositifService.searchByNomCentre(nomCentre);
                return ResponseEntity.ok(response);
        }
}