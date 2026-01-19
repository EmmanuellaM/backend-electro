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
                return LoginResponseDTO.builder()
                        .id(admin.getId())
                        .nom(admin.getNom())
                        .prenom(null) // Admin n'a pas de prénom dans l'entité
                        .email(admin.getEmail())
                        .role("admin")
                        .specialite(null)
                        .telephone(null)
                        .statut("actif")
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

                // Update last connection
                medecin.setDerniereConnexion(java.time.LocalDateTime.now());
                medecinRepository.save(medecin);

                return LoginResponseDTO.builder()
                        .id(medecin.getId())
                        .nom(medecin.getNom())
                        .prenom(medecin.getPrenom())
                        .email(medecin.getEmail())
                        .role("medecin")
                        .specialite(null) // Medecin entity doesn't have this field
                        .telephone(medecin.getTel())
                        .statut(medecin.getStatut().name())
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
        // Only for Medecins (Admins should contact system support)
        Optional<Medecin> medecinOpt = medecinRepository.findByEmail(email);
        if (medecinOpt.isEmpty()) {
            throw new com.polytechnique.backend.exception.ResourceNotFoundException(
                    "Aucun médecin trouvé avec cet email");
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

        Medecin medecin = medecinRepository.findByEmail(email)
                .orElseThrow(
                        () -> new com.polytechnique.backend.exception.ResourceNotFoundException("Médecin non trouvé"));

        medecin.setMotDePasse(newPassword); // In production, use BCrypt
        medecinRepository.save(medecin);

        // Consume token
        passwordResetTokenRepository.deleteByEmail(email);
    }
}
