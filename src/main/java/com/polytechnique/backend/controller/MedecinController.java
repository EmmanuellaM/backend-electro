package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.MedecinRequestDTO;
import com.polytechnique.backend.dto.response.MedecinResponseDTO;
import com.polytechnique.backend.service.MedecinService;
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
 * Controller REST pour gérer les médecins
 * Base URL: /api/medecins
 */
@RestController
@RequestMapping("/medecins")
@RequiredArgsConstructor
@Tag(name = "Medecin", description = "API de gestion des médecins")
public class MedecinController {

        private final MedecinService medecinService;

        /**
         * Créer un nouveau médecin
         * POST /api/medecins
         */
        @Operation(summary = "Créer un nouveau médecin", description = "Enregistre un nouveau médecin dans le système.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Médecin créé avec succès"),
                        @ApiResponse(responseCode = "400", description = "Données invalides")
        })
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
        @Operation(summary = "Récupérer un médecin par ID", description = "Retourne les détails d'un médecin spécifique.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Médecin trouvé"),
                        @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
        })
        @GetMapping("/{id}")
        public ResponseEntity<MedecinResponseDTO> getMedecinById(
                        @Parameter(description = "ID du médecin") @PathVariable int id) {
                MedecinResponseDTO response = medecinService.getMedecinById(id);
                return ResponseEntity.ok(response);
        }

        /**
         * Récupérer tous les médecins
         * GET /api/medecins
         */
        @Operation(summary = "Récupérer tous les médecins", description = "Retourne la liste complète des médecins enregistrés.")
        @ApiResponse(responseCode = "200", description = "Liste des médecins récupérée")
        @GetMapping
        public ResponseEntity<List<MedecinResponseDTO>> getAllMedecins() {
                List<MedecinResponseDTO> response = medecinService.getAllMedecins();
                return ResponseEntity.ok(response);
        }

        /**
         * Mettre à jour un médecin
         * PUT /api/medecins/{id}
         */
        @Operation(summary = "Mettre à jour un médecin", description = "Met à jour les informations d'un médecin existant.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Médecin mis à jour avec succès"),
                        @ApiResponse(responseCode = "404", description = "Médecin non trouvé"),
                        @ApiResponse(responseCode = "400", description = "Données invalides")
        })
        @PutMapping("/{id}")
        public ResponseEntity<MedecinResponseDTO> updateMedecin(
                        @Parameter(description = "ID du médecin à mettre à jour") @PathVariable int id,
                        @Valid @RequestBody MedecinRequestDTO requestDTO) {
                MedecinResponseDTO response = medecinService.updateMedecin(id, requestDTO);
                return ResponseEntity.ok(response);
        }

        /**
         * Supprimer un médecin
         * DELETE /api/medecins/{id}
         */
        @Operation(summary = "Supprimer un médecin", description = "Supprime un médecin du système.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Médecin supprimé avec succès"),
                        @ApiResponse(responseCode = "404", description = "Médecin non trouvé")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteMedecin(
                        @Parameter(description = "ID du médecin à supprimer") @PathVariable int id) {
                medecinService.deleteMedecin(id);
                return ResponseEntity.noContent().build();
        }

        /**
         * Rechercher un médecin par email
         * GET /api/medecins/email/{email}
         */
        @Operation(summary = "Rechercher un médecin par email", description = "Retourne les détails d'un médecin via son adresse email.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Médecin trouvé"),
                        @ApiResponse(responseCode = "404", description = "Médecin non trouvé avec cet email")
        })
        @GetMapping("/email/{email}")
        public ResponseEntity<MedecinResponseDTO> getMedecinByEmail(
                        @Parameter(description = "Email du médecin") @PathVariable String email) {
                MedecinResponseDTO response = medecinService.getMedecinByEmail(email);
                return ResponseEntity.ok(response);
        }

        @PatchMapping("/{id}/statut")
        @Operation(summary = "Changer le statut d'un médecin", description = "Active ou désactive un compte médecin")
        public ResponseEntity<MedecinResponseDTO> updateStatut(
                        @PathVariable int id,
                        @Valid @RequestBody com.polytechnique.backend.dto.request.StatusUpdateDTO statusDto) {
                return ResponseEntity.ok(medecinService.updateMedecinStatut(id, statusDto.getStatut()));
        }

        @GetMapping("/statut/{statut}")
        @Operation(summary = "Obtenir les médecins par statut")
        public ResponseEntity<List<MedecinResponseDTO>> getByStatut(@PathVariable String statut) {
                return ResponseEntity.ok(medecinService.getMedecinsByStatut(statut));
        }

        @GetMapping("/search")
        @Operation(summary = "Rechercher des médecins")
        public ResponseEntity<List<MedecinResponseDTO>> search(@RequestParam String query) {
                return ResponseEntity.ok(medecinService.searchMedecins(query));
        }

        @GetMapping("/{id}/statistiques")
        @Operation(summary = "Obtenir les statistiques d'un médecin")
        public ResponseEntity<com.polytechnique.backend.dto.response.MedecinStatsDTO> getStats(@PathVariable int id) {
                return ResponseEntity.ok(medecinService.getMedecinStats(id));
        }
}