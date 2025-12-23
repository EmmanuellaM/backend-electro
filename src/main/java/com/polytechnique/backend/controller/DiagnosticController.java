package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.DiagnosticRequestDTO;
import com.polytechnique.backend.dto.request.SmsDiagnosticRequest;
import com.polytechnique.backend.dto.response.DiagnosticResponseDTO;
import com.polytechnique.backend.service.DiagnosticService;
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
 * Controller REST pour gérer les diagnostics
 * Base URL: /api/diagnostics
 */
@RestController
@RequestMapping("/diagnostics")
@RequiredArgsConstructor
@Tag(name = "Diagnostic", description = "API de gestion des diagnostics")
public class DiagnosticController {

        private final DiagnosticService diagnosticService;

        /**
         * Créer un nouveau diagnostic
         * POST /api/diagnostics
         */
        @Operation(summary = "Créer un nouveau diagnostic", description = "Crée un diagnostic médical à partir des données fournies.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Diagnostic créé avec succès"),
                        @ApiResponse(responseCode = "400", description = "Données invalides fournies")
        })
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
        @Operation(summary = "Récupérer un diagnostic par ID", description = "Retourne les détails d'un diagnostic spécifié par son ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Diagnostic trouvé"),
                        @ApiResponse(responseCode = "404", description = "Diagnostic non trouvé")
        })
        @GetMapping("/{id}")
        public ResponseEntity<DiagnosticResponseDTO> getDiagnosticById(
                        @Parameter(description = "ID du diagnostic à récupérer") @PathVariable int id) {
                DiagnosticResponseDTO response = diagnosticService.getDiagnosticById(id);
                return ResponseEntity.ok(response);
        }

        /**
         * Récupérer tous les diagnostics
         * GET /api/diagnostics
         */
        @Operation(summary = "Récupérer tous les diagnostics", description = "Retourne la liste complète de tous les diagnostics enregistrés.")
        @ApiResponse(responseCode = "200", description = "Liste des diagnostics récupérée avec succès")
        @GetMapping
        public ResponseEntity<List<DiagnosticResponseDTO>> getAllDiagnostics() {
                List<DiagnosticResponseDTO> response = diagnosticService.getAllDiagnostics();
                return ResponseEntity.ok(response);
        }

        /**
         * Mettre à jour un diagnostic
         * PUT /api/diagnostics/{id}
         */
        @Operation(summary = "Mettre à jour un diagnostic", description = "Met à jour les informations d'un diagnostic existant.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Diagnostic mis à jour avec succès"),
                        @ApiResponse(responseCode = "404", description = "Diagnostic non trouvé"),
                        @ApiResponse(responseCode = "400", description = "Données invalides")
        })
        @PutMapping("/{id}")
        public ResponseEntity<DiagnosticResponseDTO> updateDiagnostic(
                        @Parameter(description = "ID du diagnostic à mettre à jour") @PathVariable int id,
                        @Valid @RequestBody DiagnosticRequestDTO requestDTO) {
                DiagnosticResponseDTO response = diagnosticService.updateDiagnostic(id, requestDTO);
                return ResponseEntity.ok(response);
        }

        /**
         * Supprimer un diagnostic
         * DELETE /api/diagnostics/{id}
         */
        @Operation(summary = "Supprimer un diagnostic", description = "Supprime un diagnostic existant de la base de données.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Diagnostic supprimé avec succès"),
                        @ApiResponse(responseCode = "404", description = "Diagnostic non trouvé")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDiagnostic(
                        @Parameter(description = "ID du diagnostic à supprimer") @PathVariable int id) {
                diagnosticService.deleteDiagnostic(id);
                return ResponseEntity.noContent().build();
        }

        /**
         * Récupérer tous les diagnostics d'un médecin
         * GET /api/diagnostics/medecin/{medecinId}
         */
        @Operation(summary = "Récupérer les diagnostics d'un médecin", description = "Retourne la liste des diagnostics associés à un médecin spécifique.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Liste des diagnostics récupérée"),
                        @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
        })
        @GetMapping("/medecin/{medecinId}")
        public ResponseEntity<List<DiagnosticResponseDTO>> getDiagnosticsByMedecin(
                        @Parameter(description = "ID du médecin") @PathVariable int medecinId) {
                List<DiagnosticResponseDTO> response = diagnosticService.getDiagnosticsByMedecin(medecinId);
                return ResponseEntity.ok(response);
        }

        /**
         * Récupérer les diagnostics basés sur des paramètres spécifiques
         * GET /api/diagnostics/parametres/{parametresId}
         */
        @Operation(summary = "Récupérer les diagnostics par paramètres", description = "Retourne les diagnostics liés à un ensemble de paramètres spécifiques.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Liste des diagnostics récupérée"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés")
        })
        @GetMapping("/parametres/{parametresId}")
        public ResponseEntity<List<DiagnosticResponseDTO>> getDiagnosticsByParametres(
                        @Parameter(description = "ID des paramètres") @PathVariable int parametresId) {
                List<DiagnosticResponseDTO> response = diagnosticService.getDiagnosticsByParametres(parametresId);
                return ResponseEntity.ok(response);
        }

        @PostMapping("/avec-notification")
        @Operation(summary = "Créer un diagnostic avec notification SMS (Simulation)")
        public ResponseEntity<Void> createDiagnosticWithNotification(
                        @Valid @RequestBody SmsDiagnosticRequest smsRequest) {
                diagnosticService.sendSmsDiagnostic(smsRequest);
                return ResponseEntity.ok().build();
        }

        @GetMapping("/recents")
        @Operation(summary = "Obtenir les diagnostics récents")
        public ResponseEntity<List<DiagnosticResponseDTO>> getRecentDiagnostics() {
                return ResponseEntity.ok(diagnosticService.getRecentDiagnostics());
        }
}