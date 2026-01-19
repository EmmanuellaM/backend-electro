package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.ChangePasswordRequestDTO;
import com.polytechnique.backend.dto.request.LoginRequestDTO;
import com.polytechnique.backend.dto.response.AdministrateurResponseDTO;
import com.polytechnique.backend.dto.response.LoginResponseDTO;
import com.polytechnique.backend.dto.response.MedecinResponseDTO;
import com.polytechnique.backend.service.AdministrateurService;
import com.polytechnique.backend.service.AuthService;
import com.polytechnique.backend.service.MedecinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour la connexion et la gestion des mots de passe")
public class AuthController {

    private final AdministrateurService administrateurService;
    private final MedecinService medecinService;
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Connexion unifiée", description = "Authentifie un utilisateur (admin ou médecin) via email et mot de passe. Retourne le rôle dans la réponse.")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/login/admin")
    @Operation(summary = "Connexion Administrateur", description = "Authentifie un administrateur via email et mot de passe.")
    public ResponseEntity<AdministrateurResponseDTO> loginAdmin(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(administrateurService.login(loginRequest));
    }

    @PostMapping("/login/medecin")
    @Operation(summary = "Connexion Médecin", description = "Authentifie un médecin via email et mot de passe.")
    public ResponseEntity<MedecinResponseDTO> loginMedecin(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(medecinService.login(loginRequest));
    }

    @PutMapping("/admin/{id}/password")
    @Operation(summary = "Changer mot de passe Admin", description = "Modifie le mot de passe d'un administrateur (nécessite l'ancien mot de passe).")
    public ResponseEntity<Void> updateAdminPassword(
            @PathVariable int id,
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
        administrateurService.updatePassword(id, changePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/medecin/{id}/password")
    @Operation(summary = "Changer mot de passe Médecin", description = "Modifie le mot de passe d'un médecin (nécessite l'ancien mot de passe).")
    public ResponseEntity<Void> updateMedecinPassword(
            @PathVariable int id,
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
        medecinService.updatePassword(id, changePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Mot de passe oublié", description = "Initie la procédure de réinitialisation de mot de passe (envoie un code).")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody com.polytechnique.backend.dto.request.EmailRequestDTO request) {
        authService.initiatePasswordReset(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    @Operation(summary = "Vérifier le code", description = "Vérifie si le code de réinitialisation est valide.")
    public ResponseEntity<Boolean> verifyCode(
            @RequestBody com.polytechnique.backend.dto.request.CodeVerifyRequestDTO request) {
        boolean isValid = authService.verifyResetToken(request.getEmail(), request.getCode());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Réinitialiser le mot de passe", description = "Définit un nouveau mot de passe si le code est valide.")
    public ResponseEntity<Void> resetPassword(
            @RequestBody com.polytechnique.backend.dto.request.ResetPasswordRequestDTO request) {
        authService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
