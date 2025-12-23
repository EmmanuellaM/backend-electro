package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.ParametresRequestDTO;
import com.polytechnique.backend.dto.response.ParametresResponseDTO;
import com.polytechnique.backend.service.ParametresService;
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
 * Controller REST pour gérer les paramètres médicaux
 * Base URL: /api/parametres
 */
@RestController
@RequestMapping("/parametres")
@RequiredArgsConstructor
@Tag(name = "Parametres", description = "API de gestion des paramètres médicaux")
public class ParametresController {

        private final ParametresService parametresService;

        /**
         * Créer de nouveaux paramètres
         * POST /api/parametres
         */
        @Operation(summary = "Créer de nouveaux paramètres", description = "Enregistre de nouveaux paramètres médicaux pour un patient.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Paramètres créés avec succès"),
                        @ApiResponse(responseCode = "400", description = "Données invalides")
        })
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
        @Operation(summary = "Récupérer des paramètres par ID", description = "Retourne les détails de paramètres spécifiques.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Paramètres trouvés"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés")
        })
        @GetMapping("/{id}")
        public ResponseEntity<ParametresResponseDTO> getParametresById(
                        @Parameter(description = "ID des paramètres") @PathVariable int id) {
                ParametresResponseDTO response = parametresService.getParametresById(id);
                return ResponseEntity.ok(response);
        }

        /**
         * Récupérer tous les paramètres
         * GET /api/parametres
         */
        @Operation(summary = "Récupérer tous les paramètres", description = "Retourne la liste complète de tous les paramètres enregistrés.")
        @ApiResponse(responseCode = "200", description = "Liste des paramètres récupérée")
        @GetMapping
        public ResponseEntity<List<ParametresResponseDTO>> getAllParametres() {
                List<ParametresResponseDTO> response = parametresService.getAllParametres();
                return ResponseEntity.ok(response);
        }

        @GetMapping("/statut/{statut}")
        @Operation(summary = "Obtenir les paramètres par statut")
        public ResponseEntity<List<ParametresResponseDTO>> getByStatut(@PathVariable String statut) {
                return ResponseEntity.ok(parametresService.getParametresByStatut(statut));
        }

        @GetMapping("/patient/{id}/historique")
        @Operation(summary = "Obtenir l'historique des paramètres d'un patient")
        public ResponseEntity<List<ParametresResponseDTO>> getHistorique(@PathVariable String id) {
                return ResponseEntity.ok(parametresService.getHistoriquePatient(id));
        }

        @GetMapping("/patient/{id}/dernieres")
        @Operation(summary = "Obtenir les dernières mesures d'un patient")
        public ResponseEntity<ParametresResponseDTO> getDernieres(@PathVariable String id) {
                return ResponseEntity.ok(parametresService.getDernieresParametresPatient(id));
        }

        @PatchMapping("/{id}/statut")
        @Operation(summary = "Mettre à jour le statut d'un paramètre")
        public ResponseEntity<ParametresResponseDTO> updateStatut(
                        @PathVariable int id,
                        @Valid @RequestBody com.polytechnique.backend.dto.request.StatusUpdateDTO statusDto) {
                return ResponseEntity.ok(parametresService.updateParametresStatut(id, statusDto.getStatut()));
        }

        /**
         * Mettre à jour des paramètres
         * PUT /api/parametres/{id}
         */
        @Operation(summary = "Mettre à jour des paramètres", description = "Met à jour les informations de paramètres existants.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Paramètres mis à jour avec succès"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés"),
                        @ApiResponse(responseCode = "400", description = "Données invalides")
        })
        @PutMapping("/{id}")
        public ResponseEntity<ParametresResponseDTO> updateParametres(
                        @Parameter(description = "ID des paramètres à mettre à jour") @PathVariable int id,
                        @Valid @RequestBody ParametresRequestDTO requestDTO) {
                ParametresResponseDTO response = parametresService.updateParametres(id, requestDTO);
                return ResponseEntity.ok(response);
        }

        /**
         * Supprimer des paramètres
         * DELETE /api/parametres/{id}
         */
        @Operation(summary = "Supprimer des paramètres", description = "Supprime des paramètres du système.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Paramètres supprimés avec succès"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteParametres(
                        @Parameter(description = "ID des paramètres à supprimer") @PathVariable int id) {
                parametresService.deleteParametres(id);
                return ResponseEntity.noContent().build();
        }

        /**
         * Récupérer tous les paramètres d'un patient
         * GET /api/parametres/patient/{identifiantPatient}
         */
        @Operation(summary = "Récupérer les paramètres d'un patient", description = "Retourne la liste des paramètres associés à un patient spécifique.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Liste des paramètres récupérée"),
                        @ApiResponse(responseCode = "404", description = "Patient non trouvé")
        })
        @GetMapping("/patient/{identifiantPatient}")
        public ResponseEntity<List<ParametresResponseDTO>> getParametresByPatient(
                        @Parameter(description = "Identifiant du patient") @PathVariable String identifiantPatient) {
                List<ParametresResponseDTO> response = parametresService.getParametresByPatient(identifiantPatient);
                return ResponseEntity.ok(response);
        }

        /**
         * Récupérer les paramètres par dispositif
         * GET /api/parametres/dispositif/{dispositifId}
         */
        @Operation(summary = "Récupérer les paramètres par dispositif", description = "Retourne la liste des paramètres relevés par un dispositif spécifique.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Liste des paramètres récupérée"),
                        @ApiResponse(responseCode = "404", description = "Dispositif non trouvé")
        })
        @GetMapping("/dispositif/{dispositifId}")
        public ResponseEntity<List<ParametresResponseDTO>> getParametresByDispositif(
                        @Parameter(description = "ID du dispositif") @PathVariable int dispositifId) {
                List<ParametresResponseDTO> response = parametresService.getParametresByDispositif(dispositifId);
                return ResponseEntity.ok(response);
        }
}