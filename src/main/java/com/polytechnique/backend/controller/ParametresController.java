package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.ParametresRequestDTO;
import com.polytechnique.backend.dto.response.ParametresResponseDTO;
import com.polytechnique.backend.service.ParametresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Paramètres Médicaux", description = "Gestion des paramètres vitaux collectés via les dispositifs IoT LoRaWAN. "
                +
                "Ces données sont décodées depuis les messages uplink et stockées avec un identifiant patient unique (DevEUI + ID Local).")
public class ParametresController {

        private final ParametresService parametresService;

        @PostMapping
        @Operation(summary = "Créer de nouveaux paramètres", description = "Enregistre un nouvel ensemble de paramètres médicaux (poids, tension, FCF, glycémie, température) pour une patiente.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Paramètres créés avec succès"),
                        @ApiResponse(responseCode = "400", description = "Données invalides")
        })
        public ResponseEntity<ParametresResponseDTO> createParametres(
                        @Valid @RequestBody ParametresRequestDTO requestDTO) {
                ParametresResponseDTO response = parametresService.createParametres(requestDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Récupérer des paramètres par ID", description = "Retourne un enregistrement de paramètres médicaux via son identifiant technique.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Paramètres trouvés"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés")
        })
        public ResponseEntity<ParametresResponseDTO> getParametresById(
                        @Parameter(description = "ID technique des paramètres", example = "1") @PathVariable int id) {
                ParametresResponseDTO response = parametresService.getParametresById(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping
        @Operation(summary = "Lister tous les paramètres", description = "Retourne la liste de tous les enregistrements de paramètres médicaux.")
        @ApiResponse(responseCode = "200", description = "Liste des paramètres")
        public ResponseEntity<List<ParametresResponseDTO>> getAllParametres() {
                List<ParametresResponseDTO> response = parametresService.getAllParametres();
                return ResponseEntity.ok(response);
        }

        @GetMapping("/pending")
        @Operation(summary = "Lister les paramètres sans diagnostic", description = "Retourne la liste des paramètres médicaux qui n'ont pas encore de diagnostic associé.")
        @ApiResponse(responseCode = "200", description = "Liste des paramètres en attente de diagnostic")
        public ResponseEntity<List<ParametresResponseDTO>> getParametresSansDiagnostic(
                        @Parameter(description = "ID de l'admin (pour filtrage)", example = "1") @RequestParam(required = false) Integer adminId) {
                List<ParametresResponseDTO> response = parametresService.getParametresSansDiagnostic(adminId);
                return ResponseEntity.ok(response);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Mettre à jour des paramètres", description = "Modifie un enregistrement de paramètres médicaux existant.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Paramètres mis à jour"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés"),
                        @ApiResponse(responseCode = "400", description = "Données invalides")
        })
        public ResponseEntity<ParametresResponseDTO> updateParametres(
                        @Parameter(description = "ID technique des paramètres", example = "1") @PathVariable int id,
                        @Valid @RequestBody ParametresRequestDTO requestDTO) {
                ParametresResponseDTO response = parametresService.updateParametres(id, requestDTO);
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Supprimer des paramètres", description = "Supprime un enregistrement de paramètres médicaux.")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Paramètres supprimés"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés")
        })
        public ResponseEntity<Void> deleteParametres(
                        @Parameter(description = "ID technique des paramètres", example = "1") @PathVariable int id) {
                parametresService.deleteParametres(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/patient/{identifiantPatient}")
        @Operation(summary = "Paramètres par patient", description = "Retourne l'historique des paramètres médicaux d'une patiente via son identifiant unique (DevEUI + ID Local, ex: 'DispositifA-P05').")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Liste des paramètres du patient"),
                        @ApiResponse(responseCode = "404", description = "Patient non trouvé")
        })
        public ResponseEntity<List<ParametresResponseDTO>> getParametresByPatient(
                        @Parameter(description = "Identifiant unique du patient (DevEUI + ID Local)", example = "DispositifA-P05") @PathVariable String identifiantPatient) {
                List<ParametresResponseDTO> response = parametresService.getParametresByPatient(identifiantPatient);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/dispositif/{dispositifId}")
        @Operation(summary = "Paramètres par dispositif", description = "Retourne tous les paramètres médicaux collectés par un dispositif IoT spécifique.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Liste des paramètres du dispositif"),
                        @ApiResponse(responseCode = "404", description = "Dispositif non trouvé")
        })
        public ResponseEntity<List<ParametresResponseDTO>> getParametresByDispositif(
                        @Parameter(description = "ID du dispositif IoT", example = "1") @PathVariable int dispositifId) {
                List<ParametresResponseDTO> response = parametresService.getParametresByDispositif(dispositifId);
                return ResponseEntity.ok(response);
        }

        // === Endpoints de verrouillage ===

        @PostMapping("/{id}/lock")
        @Operation(summary = "Verrouiller des paramètres", description = "Verrouille un ensemble de paramètres pour empêcher l'accès concurrent par d'autres médecins. Le verrou expire après 30 minutes.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Paramètres verrouillés"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés"),
                        @ApiResponse(responseCode = "409", description = "Déjà verrouillé par un autre médecin")
        })
        public ResponseEntity<ParametresResponseDTO> lockParametres(
                        @Parameter(description = "ID des paramètres", example = "1") @PathVariable int id,
                        @Parameter(description = "ID du médecin", example = "1") @RequestParam int medecinId) {
                ParametresResponseDTO response = parametresService.lockParametres(id, medecinId);
                return ResponseEntity.ok(response);
        }

        @PostMapping("/{id}/unlock")
        @Operation(summary = "Déverrouiller des paramètres", description = "Libère le verrou sur des paramètres pour permettre l'accès à d'autres médecins.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Paramètres déverrouillés"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés")
        })
        public ResponseEntity<ParametresResponseDTO> unlockParametres(
                        @Parameter(description = "ID des paramètres", example = "1") @PathVariable int id) {
                ParametresResponseDTO response = parametresService.unlockParametres(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/lock-status")
        @Operation(summary = "Vérifier le statut de verrouillage", description = "Vérifie si des paramètres sont verrouillés par un autre médecin.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Statut de verrouillage retourné"),
                        @ApiResponse(responseCode = "404", description = "Paramètres non trouvés")
        })
        public ResponseEntity<Boolean> checkLockStatus(
                        @Parameter(description = "ID des paramètres", example = "1") @PathVariable int id,
                        @Parameter(description = "ID du médecin qui vérifie", example = "1") @RequestParam int medecinId) {
                boolean isLocked = parametresService.isLocked(id, medecinId);
                return ResponseEntity.ok(isLocked);
        }
}