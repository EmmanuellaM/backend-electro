package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.LoginRequestDTO;
import com.polytechnique.backend.dto.response.LoginResponseDTO;
import com.polytechnique.backend.entity.Administrateur;
import com.polytechnique.backend.entity.Medecin;
import com.polytechnique.backend.exception.AuthenticationException;
import com.polytechnique.backend.repository.AdministrateurRepository;
import com.polytechnique.backend.repository.MedecinRepository;
import com.polytechnique.backend.service.AuthService;
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
                return LoginResponseDTO.builder()
                        .id(medecin.getId())
                        .nom(medecin.getNom())
                        .prenom(medecin.getPrenom())
                        .email(medecin.getEmail())
                        .role("medecin")
                        .specialite(null) // Medecin entity doesn't have this field
                        .telephone(medecin.getTel())
                        .statut(medecin.getStatut())
                        .build();
            } else {
                throw new AuthenticationException("Mot de passe incorrect");
            }
        }

        // 3. Utilisateur non trouvé
        throw new AuthenticationException("Utilisateur non trouvé avec cet email");
    }
}
