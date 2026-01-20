package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.InfirmierLocalRequestDTO;
import com.polytechnique.backend.dto.response.InfirmierLocalResponseDTO;
import com.polytechnique.backend.service.InfirmierLocalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/infirmiers")
@RequiredArgsConstructor
@Tag(name = "Infirmiers Locaux", description = "Gestion des infirmiers locaux responsables des zones et dispositifs")
public class InfirmierLocalController {

    private final InfirmierLocalService infirmierLocalService;

    @PostMapping
    public ResponseEntity<InfirmierLocalResponseDTO> createInfirmier(
            @Valid @RequestBody InfirmierLocalRequestDTO requestDTO) {
        InfirmierLocalResponseDTO response = infirmierLocalService.createInfirmier(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un infirmier", description = "Retourne les détails d'un infirmier par son ID.")
    public ResponseEntity<InfirmierLocalResponseDTO> getInfirmierById(@PathVariable int id) {
        return ResponseEntity.ok(infirmierLocalService.getInfirmierById(id));
    }

    @GetMapping
    @Operation(summary = "Lister les infirmiers", description = "Retourne la liste de tous les infirmiers locaux enregistrés.")
    public ResponseEntity<List<InfirmierLocalResponseDTO>> getAllInfirmiers(
            @RequestParam(required = false) Integer adminId) {
        return ResponseEntity.ok(infirmierLocalService.getAllInfirmiers(adminId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un infirmier", description = "Modifie les informations d'un infirmier existant.")
    public ResponseEntity<InfirmierLocalResponseDTO> updateInfirmier(
            @PathVariable int id,
            @Valid @RequestBody InfirmierLocalRequestDTO requestDTO) {
        return ResponseEntity.ok(infirmierLocalService.updateInfirmier(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un infirmier", description = "Supprime définitivement un infirmier de la base de données.")
    public ResponseEntity<Void> deleteInfirmier(@PathVariable int id) {
        infirmierLocalService.deleteInfirmier(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Modifier le statut d'un infirmier", description = "Active ou désactive un infirmier.")
    public ResponseEntity<InfirmierLocalResponseDTO> updateStatut(
            @PathVariable int id,
            @RequestBody java.util.Map<String, String> body) {
        String statut = body.get("statut");
        return ResponseEntity.ok(infirmierLocalService.updateStatut(id, statut));
    }
}
