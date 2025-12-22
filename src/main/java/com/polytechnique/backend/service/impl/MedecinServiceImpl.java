package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.MedecinRequestDTO;
import com.polytechnique.backend.dto.response.MedecinResponseDTO;
import com.polytechnique.backend.entity.Medecin;
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

    @Override
    public MedecinResponseDTO createMedecin(MedecinRequestDTO requestDTO) {
        // Vérifier si l'email existe déjà
        if (medecinRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(requestDTO.getEmail());
        }

        // Convertir DTO → Entité
        Medecin medecin = medecinMapper.toEntity(requestDTO);

        // Sauvegarder
        Medecin savedMedecin = medecinRepository.save(medecin);

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
    public List<MedecinResponseDTO> getAllMedecins() {
        return medecinRepository.findAll()
                .stream()
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
}