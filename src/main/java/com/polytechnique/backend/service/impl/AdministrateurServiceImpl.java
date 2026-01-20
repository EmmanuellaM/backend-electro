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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministrateurServiceImpl implements AdministrateurService {

    private final AdministrateurRepository administrateurRepository;
    private final AdministrateurMapper administrateurMapper;

    @Override
    @Transactional
    public AdministrateurResponseDTO createAdministrateur(AdministrateurRequestDTO requestDTO) {
        if (administrateurRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Un administrateur avec cet email existe déjà.");
        }

        Administrateur admin = administrateurMapper.toEntity(requestDTO);
        // Note: Password encoding should be handled here if Security is active (e.g.
        // BCrypt)
        // admin.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));

        Administrateur savedAdmin = administrateurRepository.save(admin);
        return administrateurMapper.toResponseDTO(savedAdmin);
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
        Administrateur updatedAdmin = administrateurRepository.save(admin);
        return administrateurMapper.toResponseDTO(updatedAdmin);
    }

    @Override
    @Transactional
    public void deleteAdministrateur(int id) {
        if (!administrateurRepository.existsById(id)) {
            throw new EntityNotFoundException("Administrateur non trouvé avec l'ID : " + id);
        }
        administrateurRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AdministrateurResponseDTO login(LoginRequestDTO loginRequest) {
        Administrateur admin = administrateurRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Email ou mot de passe incorrect."));

        // Simple string comparison (In production, use BCrypt:
        // passwordEncoder.matches(...))
        if (!admin.getMotDePasse().equals(loginRequest.getMotDePasse())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }

        return administrateurMapper.toResponseDTO(admin);
    }

    @Override
    @Transactional
    public void updatePassword(int id, ChangePasswordRequestDTO changePasswordRequest) {
        Administrateur admin = administrateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrateur non trouvé avec l'ID : " + id));

        if (!admin.getMotDePasse().equals(changePasswordRequest.getAncienMotDePasse())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect.");
        }

        admin.setMotDePasse(changePasswordRequest.getNouveauMotDePasse());
        administrateurRepository.save(admin);
    }

    @Override
    @Transactional
    public AdministrateurResponseDTO updateStatut(int id, String statut) {
        Administrateur admin = administrateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrateur non trouvé avec l'ID : " + id));

        try {
            com.polytechnique.backend.entity.StatutAdministrateur newStatut = com.polytechnique.backend.entity.StatutAdministrateur
                    .valueOf(statut.toUpperCase());
            admin.setStatut(newStatut);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide : " + statut);
        }

        Administrateur updatedAdmin = administrateurRepository.save(admin);
        return administrateurMapper.toResponseDTO(updatedAdmin);
    }
}
