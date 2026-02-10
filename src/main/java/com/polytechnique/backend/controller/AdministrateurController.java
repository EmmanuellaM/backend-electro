package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.AdministrateurRequestDTO;
import com.polytechnique.backend.dto.response.AdministrateurResponseDTO;
import com.polytechnique.backend.service.AdministrateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@RequestMapping("/administrateurs")
@RequiredArgsConstructor
@Tag(name = "Administrateurs", description = "Gestion des comptes administrateurs")
public class AdministrateurController {

    private final AdministrateurService administrateurService;

    @PostMapping
    @Operation(summary = "Créer un administrateur", description = "Crée un nouveau compte administrateur.")
    public ResponseEntity<AdministrateurResponseDTO> createAdministrateur(
            @Valid @RequestBody AdministrateurRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(administrateurService.createAdministrateur(requestDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un administrateur", description = "Retourne les détails d'un administrateur par ID.")
    public ResponseEntity<AdministrateurResponseDTO> getAdministrateurById(@PathVariable int id) {
        return ResponseEntity.ok(administrateurService.getAdministrateurById(id));
    }

    @GetMapping
    @Operation(summary = "Lister les administrateurs", description = "Retourne la liste de tous les administrateurs.")
    public ResponseEntity<List<AdministrateurResponseDTO>> getAllAdministrateurs() {
        return ResponseEntity.ok(administrateurService.getAllAdministrateurs());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un administrateur", description = "Modifie les informations d'un administrateur.")
    public ResponseEntity<AdministrateurResponseDTO> updateAdministrateur(
            @PathVariable int id,
            @Valid @RequestBody AdministrateurRequestDTO requestDTO) {
        return ResponseEntity.ok(administrateurService.updateAdministrateur(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un administrateur", description = "Supprime un compte administrateur.")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable int id) {
        administrateurService.deleteAdministrateur(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Modifier le statut d'un administrateur", description = "Suspendre ou Activer un administrateur.")
    public ResponseEntity<AdministrateurResponseDTO> updateStatut(
            @PathVariable int id,
            @RequestParam @Parameter(description = "ACTIF ou SUSPENDU") String statut) {
        return ResponseEntity.ok(administrateurService.updateStatut(id, statut));
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Changer le mot de passe", description = "Permet à l'administrateur de changer son mot de passe (notamment lors de la première connexion).")
    public ResponseEntity<Void> updatePassword(
            @PathVariable int id,
            @Valid @RequestBody com.polytechnique.backend.dto.request.ChangePasswordRequestDTO changePasswordRequest) {
        administrateurService.updatePassword(id, changePasswordRequest);
        return ResponseEntity.ok().build();
    }
}
