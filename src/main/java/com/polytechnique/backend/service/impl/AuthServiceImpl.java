package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.LoginRequestDTO;
import com.polytechnique.backend.dto.response.LoginResponseDTO;
import com.polytechnique.backend.entity.Administrateur;
import com.polytechnique.backend.entity.Medecin;
import com.polytechnique.backend.exception.AuthenticationException;
import com.polytechnique.backend.repository.AdministrateurRepository;
import com.polytechnique.backend.repository.MedecinRepository;
import com.polytechnique.backend.service.AuthService;
import com.polytechnique.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AdministrateurRepository administrateurRepository;
    private final MedecinRepository medecinRepository;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        String email = loginRequest.getEmail();
        String motDePasse = loginRequest.getMotDePasse();

        // 1. Chercher dans la table Administrateur
        Optional<Administrateur> adminOpt = administrateurRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Administrateur admin = adminOpt.get();
            // Vérifier le mot de passe
            if (admin.getMotDePasse().equals(motDePasse)) {
                // Check if suspended
                if (com.polytechnique.backend.entity.StatutAdministrateur.SUSPENDU.equals(admin.getStatut())) {
                    throw new AuthenticationException("Votre compte est suspendu. Veuillez contacter le Super Admin.");
                }

                return LoginResponseDTO.builder()
                        .id(admin.getId())
                        .nom(admin.getNom())
                        .prenom(null) // Admin n'a pas de prénom dans l'entité
                        .email(admin.getEmail())
                        .role(admin.getRole() != null ? admin.getRole().name() : "ADMIN")
                        .specialite(null)
                        .telephone(null)
                        .statut("actif") // Statut for frontend
                        .build();
            } else {
                throw new AuthenticationException("Mot de passe incorrect");
            }
        }

        // 2. Chercher dans la table Medecin
        Optional<Medecin> medecinOpt = medecinRepository.findByEmail(email);
        if (medecinOpt.isPresent()) {
            Medecin medecin = medecinOpt.get();
            // Vérifier le mot de passe
            if (medecin.getMotDePasse().equals(motDePasse)) {
                // Vérifier le statut du compte
                if (com.polytechnique.backend.entity.StatutMedecin.INACTIF.equals(medecin.getStatut())) {
                    throw new AuthenticationException("Votre compte a été désactivé. Contactez l'administrateur.");
                }
                if (com.polytechnique.backend.entity.StatutMedecin.SUSPENDU.equals(medecin.getStatut())) {
                    throw new AuthenticationException(
                            "Votre compte est temporairement suspendu. Contactez l'administrateur.");
                }

                // Check if associated Admin is suspended
                if (medecin.getAdministrateur() != null
                        && com.polytechnique.backend.entity.StatutAdministrateur.SUSPENDU
                                .equals(medecin.getAdministrateur().getStatut())) {
                    throw new AuthenticationException(
                            "Votre administrateur est suspendu. Veuillez le contacter pour plus d'informations.");
                }

                // Update last connection
                medecin.setDerniereConnexion(java.time.LocalDateTime.now());
                medecinRepository.save(medecin);

                return LoginResponseDTO.builder()
                        .id(medecin.getId())
                        .nom(medecin.getNom())
                        .prenom(medecin.getPrenom())
                        .email(medecin.getEmail())
                        .role("medecin")
                        .specialite(medecin.getSpecialite() != null ? medecin.getSpecialite().name() : null)
                        .telephone(medecin.getTel())
                        .statut(medecin.getStatut() != null ? medecin.getStatut().name() : "ACTIF")
                        // Ajouter l'ID de l'admin
                        .administrateurId(
                                medecin.getAdministrateur() != null ? medecin.getAdministrateur().getId() : null)
                        .build();
            } else {
                throw new AuthenticationException("Mot de passe incorrect");
            }
        }

        // 3. Utilisateur non trouvé
        throw new AuthenticationException("Utilisateur non trouvé avec cet email");
    }

    private final com.polytechnique.backend.repository.PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void initiatePasswordReset(String email) {
        boolean distinctUserFound = false;

        // 1. Check Medecin
        Optional<Medecin> medecinOpt = medecinRepository.findByEmail(email);
        if (medecinOpt.isPresent()) {
            distinctUserFound = true;
        } else {
            // 2. Check Administrateur
            Optional<Administrateur> adminOpt = administrateurRepository.findByEmail(email);
            if (adminOpt.isPresent()) {
                Administrateur admin = adminOpt.get();
                if (com.polytechnique.backend.entity.Role.SUPER_ADMIN.equals(admin.getRole())) {
                    throw new com.polytechnique.backend.exception.AuthenticationException(
                            "Le Super Admin ne peut pas réinitialiser son mot de passe par email. Contactez le support technique.");
                }
                distinctUserFound = true;
            }
        }

        if (!distinctUserFound) {
            throw new com.polytechnique.backend.exception.ResourceNotFoundException(
                    "Aucun utilisateur trouvé avec cet email");
        }

        // Delete existing token if any
        passwordResetTokenRepository.deleteByEmail(email);

        // Generate 6-digit code
        String token = String.format("%06d", new java.util.Random().nextInt(999999));

        // Save token
        com.polytechnique.backend.entity.PasswordResetToken resetToken = new com.polytechnique.backend.entity.PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryDate(java.time.LocalDateTime.now().plusMinutes(15));
        passwordResetTokenRepository.save(resetToken);

        // Send email with reset code
        emailService.sendPasswordResetCode(email, token);
    }

    @Override
    public boolean verifyResetToken(String email, String token) {
        Optional<com.polytechnique.backend.entity.PasswordResetToken> tokenOpt = passwordResetTokenRepository
                .findByEmailAndToken(email, token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        com.polytechnique.backend.entity.PasswordResetToken resetToken = tokenOpt.get();
        return resetToken.getExpiryDate().isAfter(java.time.LocalDateTime.now());
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void resetPassword(String email, String token, String newPassword) {
        if (!verifyResetToken(email, token)) {
            throw new com.polytechnique.backend.exception.AuthenticationException(
                    "Code de vérification invalide ou expiré");
        }

        boolean passwordUpdated = false;

        // 1. Try Medecin
        Optional<Medecin> medecinOpt = medecinRepository.findByEmail(email);
        if (medecinOpt.isPresent()) {
            Medecin medecin = medecinOpt.get();
            medecin.setMotDePasse(newPassword);
            medecinRepository.save(medecin);
            passwordUpdated = true;
        }

        // 2. Try Admin if not Medecin (or both if email shared? assuming unique email
        // across system or priority)
        if (!passwordUpdated) {
            Optional<Administrateur> adminOpt = administrateurRepository.findByEmail(email);
            if (adminOpt.isPresent()) {
                Administrateur admin = adminOpt.get();
                if (com.polytechnique.backend.entity.Role.SUPER_ADMIN.equals(admin.getRole())) {
                    throw new com.polytechnique.backend.exception.AuthenticationException(
                            "Impossible de réinitialiser le mot de passe du Super Admin.");
                }
                admin.setMotDePasse(newPassword);
                administrateurRepository.save(admin);
                passwordUpdated = true;
            }
        }

        if (!passwordUpdated) {
            throw new com.polytechnique.backend.exception.ResourceNotFoundException("Utilisateur non trouvé");
        }

        // Consume token
        passwordResetTokenRepository.deleteByEmail(email);
    }
}
