package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.AdministrateurRequestDTO;
import com.polytechnique.backend.dto.request.ChangePasswordRequestDTO;
import com.polytechnique.backend.dto.request.LoginRequestDTO;
import com.polytechnique.backend.dto.response.AdministrateurResponseDTO;
import com.polytechnique.backend.entity.Administrateur;
import com.polytechnique.backend.mapper.AdministrateurMapper;
import com.polytechnique.backend.repository.AdministrateurRepository;
import com.polytechnique.backend.service.AdministrateurService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministrateurServiceImpl implements AdministrateurService {

    private final AdministrateurRepository administrateurRepository;
    private final AdministrateurMapper administrateurMapper;
    private final PasswordEncoder passwordEncoder;
    private final com.polytechnique.backend.service.EmailService emailService;

    @Override
    @Transactional
    public AdministrateurResponseDTO createAdministrateur(AdministrateurRequestDTO requestDTO) {
        if (administrateurRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Un administrateur avec cet email existe déjà.");
        }

        Administrateur admin = administrateurMapper.toEntity(requestDTO);

        // Générer un mot de passe temporaire à usage unique
        String tempPassword = java.util.UUID.randomUUID().toString().substring(0, 8);
        admin.setMotDePasse(passwordEncoder.encode(tempPassword));
        admin.setDoitChangerMotDePasse(true);

        // Envoi du mail de bienvenue avec les identifiants
        emailService.sendAdminAccountEmail(admin.getEmail(), admin.getNom(), tempPassword);

        Administrateur savedAdmin = administrateurRepository.save(admin);
        AdministrateurResponseDTO response = administrateurMapper.toResponseDTO(savedAdmin);
        response.setTempPassword(tempPassword);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public AdministrateurResponseDTO getAdministrateurById(int id) {
        Administrateur admin = administrateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrateur non trouvé avec l'ID : " + id));
        return administrateurMapper.toResponseDTO(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministrateurResponseDTO> getAllAdministrateurs() {
        return administrateurRepository.findAll().stream()
                .filter(a -> a.getStatut() != com.polytechnique.backend.status.StatutAdministrateur.SUPPRIME)
                .map(administrateurMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdministrateurResponseDTO updateAdministrateur(int id, AdministrateurRequestDTO requestDTO) {
        Administrateur admin = administrateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrateur non trouvé avec l'ID : " + id));

        if (requestDTO.getEmail() != null && !requestDTO.getEmail().equals(admin.getEmail())) {
            if (administrateurRepository.existsByEmail(requestDTO.getEmail())) {
                throw new IllegalArgumentException("Cet email est déjà utilisé par un autre administrateur.");
            }
        }

        administrateurMapper.updateEntity(requestDTO, admin);

        // Si un nouveau mot de passe est fourni, le hasher
        if (requestDTO.getMotDePasse() != null && !requestDTO.getMotDePasse().isEmpty()) {
            admin.setMotDePasse(passwordEncoder.encode(requestDTO.getMotDePasse()));
        }

        Administrateur updatedAdmin = administrateurRepository.save(admin);
        return administrateurMapper.toResponseDTO(updatedAdmin);
    }

    @Override
    @Transactional
    public void deleteAdministrateur(int id) {
        Administrateur admin = administrateurRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Administrateur non trouvé avec l'ID : " + id));
        admin.setStatut(com.polytechnique.backend.status.StatutAdministrateur.SUPPRIME);
        administrateurRepository.save(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public AdministrateurResponseDTO login(LoginRequestDTO loginRequest) {
        Administrateur admin = administrateurRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Email ou mot de passe incorrect."));

        // Vérification du mot de passe avec BCrypt
        if (!passwordEncoder.matches(loginRequest.getMotDePasse(), admin.getMotDePasse())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }

        // Vérifier si le compte est supprimé
        if (admin.getStatut() == com.polytechnique.backend.status.StatutAdministrateur.SUPPRIME) {
            throw new IllegalArgumentException("Ce compte a été supprimé.");
        }

        return administrateurMapper.toResponseDTO(admin);
    }

    @Override
    @Transactional
    public void updatePassword(int id, ChangePasswordRequestDTO changePasswordRequest) {
        Administrateur admin = administrateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrateur non trouvé avec l'ID : " + id));

        // Vérification de l'ancien mot de passe avec BCrypt
        if (!passwordEncoder.matches(changePasswordRequest.getAncienMotDePasse().trim(), admin.getMotDePasse())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect.");
        }

        // Hasher le nouveau mot de passe
        admin.setMotDePasse(passwordEncoder.encode(changePasswordRequest.getNouveauMotDePasse()));
        admin.setDoitChangerMotDePasse(false); // L'admin a changé son MDP
        administrateurRepository.save(admin);
    }

    @Override
    @Transactional
    public AdministrateurResponseDTO updateStatut(int id, String statut) {
        Administrateur admin = administrateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrateur non trouvé avec l'ID : " + id));

        try {
            com.polytechnique.backend.status.StatutAdministrateur newStatut = com.polytechnique.backend.status.StatutAdministrateur
                    .valueOf(statut.toUpperCase());
            admin.setStatut(newStatut);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide : " + statut);
        }

        Administrateur updatedAdmin = administrateurRepository.save(admin);
        return administrateurMapper.toResponseDTO(updatedAdmin);
    }
}
