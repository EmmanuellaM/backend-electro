package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.DispositifRequestDTO;
import com.polytechnique.backend.dto.response.DispositifResponseDTO;
import com.polytechnique.backend.service.DispositifService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Dispositifs", description = "Gestion des dispositifs IoT de collecte de données médicales")
public class DispositifController {

    private final DispositifService dispositifService;

    @PostMapping
    @Operation(summary = "Créer un dispositif", description = "Enregistre un nouveau dispositif avec son DevEUI et l'infirmier assigné.")
    public ResponseEntity<DispositifResponseDTO> createDispositif(
            @Valid @RequestBody DispositifRequestDTO requestDTO) {
        DispositifResponseDTO response = dispositifService.createDispositif(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un dispositif par ID", description = "Retourne les détails d'un dispositif incluant l'infirmier associé.")
    public ResponseEntity<DispositifResponseDTO> getDispositifById(@PathVariable int id) {
        DispositifResponseDTO response = dispositifService.getDispositifById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Lister tous les dispositifs", description = "Retourne la liste complète des dispositifs déployés.")
    public ResponseEntity<List<DispositifResponseDTO>> getAllDispositifs() {
        List<DispositifResponseDTO> response = dispositifService.getAllDispositifs();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un dispositif", description = "Modifie les informations d'un dispositif ou change l'infirmier assigné.")
    public ResponseEntity<DispositifResponseDTO> updateDispositif(
            @PathVariable int id,
            @Valid @RequestBody DispositifRequestDTO requestDTO) {
        DispositifResponseDTO response = dispositifService.updateDispositif(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un dispositif", description = "Supprime définitivement un dispositif de la base de données.")
    public ResponseEntity<Void> deleteDispositif(@PathVariable int id) {
        dispositifService.deleteDispositif(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Modifier le statut d'un dispositif", description = "Change uniquement le statut d'un dispositif. Valeurs possibles: ACTIF (opérationnel), INACTIF (désactivé), MAINTENANCE (en réparation).")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nouveau statut du dispositif", required = true, content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.polytechnique.backend.dto.request.StatutDispositifRequestDTO.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Activer un dispositif", value = "{\"statut\": \"ACTIF\"}")))
    public ResponseEntity<DispositifResponseDTO> updateStatut(
            @PathVariable int id,
            @RequestBody com.polytechnique.backend.dto.request.StatutDispositifRequestDTO body) {
        String newStatut = body.getStatut().name();
        DispositifResponseDTO response = dispositifService.updateStatut(id, newStatut);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activer")
    @Operation(summary = "Activer un dispositif", description = "Assigne un infirmier et active un dispositif en attente.")
    public ResponseEntity<DispositifResponseDTO> activerDispositif(
            @PathVariable int id,
            @jakarta.validation.Valid @RequestBody com.polytechnique.backend.dto.request.ActivationDispositifRequestDTO body) {
        DispositifResponseDTO response = dispositifService.activerDispositif(id, body);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher par centre de santé", description = "Trouve des dispositifs par nom de centre de santé.")
    public ResponseEntity<List<DispositifResponseDTO>> searchByNomCentre(
            @RequestParam String nomCentre) {
        List<DispositifResponseDTO> response = dispositifService.searchByNomCentre(nomCentre);
        return ResponseEntity.ok(response);
    }
}