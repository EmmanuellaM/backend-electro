package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.ChangePasswordRequestDTO;
import com.polytechnique.backend.dto.request.LoginRequestDTO;
import com.polytechnique.backend.dto.request.MedecinRequestDTO;
import com.polytechnique.backend.dto.response.MedecinResponseDTO;
import com.polytechnique.backend.entity.Medecin;
import com.polytechnique.backend.entity.StatutMedecin;
import com.polytechnique.backend.exception.EmailAlreadyExistsException;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.mapper.MedecinMapper;
import com.polytechnique.backend.repository.MedecinRepository;
import com.polytechnique.backend.service.MedecinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service pour gérer les médecins
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MedecinServiceImpl implements MedecinService {

    private final MedecinRepository medecinRepository;
    private final MedecinMapper medecinMapper;
    private final com.polytechnique.backend.repository.AdministrateurRepository administrateurRepository;
    private final com.polytechnique.backend.service.EmailService emailService;

    @Override
    public MedecinResponseDTO createMedecin(MedecinRequestDTO requestDTO) {
        // Vérifier si l'email existe déjà
        if (medecinRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(requestDTO.getEmail());
        }

        // Convertir DTO → Entité
        Medecin medecin = medecinMapper.toEntity(requestDTO);

        // Lier l'administrateur
        if (requestDTO.getAdministrateurId() != null) {
            com.polytechnique.backend.entity.Administrateur admin = administrateurRepository
                    .findById(requestDTO.getAdministrateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Administrateur", "id",
                            requestDTO.getAdministrateurId()));
            medecin.setAdministrateur(admin);
        }

        // Générer un mot de passe si non fourni
        String rawPassword = requestDTO.getMotDePasse();
        boolean isGeneratedPassword = false;
        if (rawPassword == null || rawPassword.isBlank()) {
            // Générer un mot de passe aléatoire (8 caractères)
            rawPassword = java.util.UUID.randomUUID().toString().substring(0, 8);
            System.out.println("DEBUG - NEW MEDECIN PASSWORD: [" + rawPassword + "]");
            medecin.setMotDePasse(rawPassword);
            isGeneratedPassword = true;
        }

        // Sauvegarder
        Medecin savedMedecin = medecinRepository.save(medecin);

        // Envoyer l'email avec le mot de passe (si généré ou si on décide de tout le
        // temps l'envoyer)
        // Ici on envoie toujours pour confirmer la création + identifiants
        try {
            emailService.sendNewAccountEmail(savedMedecin.getEmail(), savedMedecin.getNom(), rawPassword);
        } catch (Exception e) {
            // Loguer l'erreur mais ne pas faire échouer la transaction car le compte est
            // créé
            // TODO: Gérer plus proprement (queue, retry)
            System.err.println("Erreur envoi email: " + e.getMessage());
        }

        // Convertir Entité → DTO de réponse
        return medecinMapper.toResponseDTO(savedMedecin);
    }

    @Override
    @Transactional(readOnly = true)
    public MedecinResponseDTO getMedecinById(int id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin", "id", id));

        return medecinMapper.toResponseDTO(medecin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedecinResponseDTO> getAllMedecins(Integer adminId) {
        List<Medecin> medecins;
        if (adminId != null) {
            medecins = medecinRepository.findByAdministrateurId(adminId);
        } else {
            medecins = medecinRepository.findAll();
        }
        return medecins.stream()
                .map(medecinMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedecinResponseDTO updateMedecin(int id, MedecinRequestDTO requestDTO) {
        // Récupérer le médecin existant
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin", "id", id));

        // Vérifier si le nouvel email n'est pas déjà utilisé par un autre médecin
        if (!medecin.getEmail().equals(requestDTO.getEmail()) &&
                medecinRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(requestDTO.getEmail());
        }

        // Mettre à jour l'entité
        medecinMapper.updateEntity(requestDTO, medecin);

        // Mettre à jour l'administrateur si nécessaire
        if (requestDTO.getAdministrateurId() != null) {
            com.polytechnique.backend.entity.Administrateur admin = administrateurRepository
                    .findById(requestDTO.getAdministrateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Administrateur", "id",
                            requestDTO.getAdministrateurId()));
            medecin.setAdministrateur(admin);
        }

        // Sauvegarder
        Medecin updatedMedecin = medecinRepository.save(medecin);

        return medecinMapper.toResponseDTO(updatedMedecin);
    }

    @Override
    public void deleteMedecin(int id) {
        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(id)) {
            throw new ResourceNotFoundException("Médecin", "id", id);
        }

        medecinRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MedecinResponseDTO getMedecinByEmail(String email) {
        Medecin medecin = medecinRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin", "email", email));

        return medecinMapper.toResponseDTO(medecin);
    }

    @Override
    @Transactional(readOnly = true)
    public MedecinResponseDTO login(LoginRequestDTO loginRequest) {
        Medecin medecin = medecinRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Médecin", "email", loginRequest.getEmail()));

        // Check password (handling null password for created users who haven't set one
        // yet?)
        if (medecin.getMotDePasse() == null || !medecin.getMotDePasse().equals(loginRequest.getMotDePasse())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }

        return medecinMapper.toResponseDTO(medecin);
    }

    @Override
    @Transactional
    public void updatePassword(int id, ChangePasswordRequestDTO changePasswordRequest) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin", "id", id));

        // If password was never set, maybe allow setting it without "old" password?
        // For security, if old password exists, it must match.
        if (medecin.getMotDePasse() != null
                && !medecin.getMotDePasse().equals(changePasswordRequest.getAncienMotDePasse())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect.");
        }

        medecin.setMotDePasse(changePasswordRequest.getNouveauMotDePasse());
        medecinRepository.save(medecin);
    }

    @Override
    public MedecinResponseDTO updateStatut(int id, String statut) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin", "id", id));

        medecin.setStatut(StatutMedecin.valueOf(statut.toUpperCase()));
        Medecin updatedMedecin = medecinRepository.save(medecin);

        return medecinMapper.toResponseDTO(updatedMedecin);
    }
}