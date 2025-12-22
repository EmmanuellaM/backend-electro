package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.DispositifRequestDTO;
import com.polytechnique.backend.dto.response.DispositifResponseDTO;
import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.mapper.DispositifMapper;
import com.polytechnique.backend.repository.DispositifRepository;
import com.polytechnique.backend.service.DispositifService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service pour gérer les dispositifs
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DispositifServiceImpl implements DispositifService {

    private final DispositifRepository dispositifRepository;
    private final DispositifMapper dispositifMapper;

    @Override
    public DispositifResponseDTO createDispositif(DispositifRequestDTO requestDTO) {
        // Convertir DTO → Entité
        Dispositif dispositif = dispositifMapper.toEntity(requestDTO);

        // Sauvegarder
        Dispositif savedDispositif = dispositifRepository.save(dispositif);

        // Convertir Entité → DTO de réponse
        return dispositifMapper.toResponseDTO(savedDispositif);
    }

    @Override
    @Transactional(readOnly = true)
    public DispositifResponseDTO getDispositifById(int id) {
        Dispositif dispositif = dispositifRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositif", "id", id));

        return dispositifMapper.toResponseDTO(dispositif);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DispositifResponseDTO> getAllDispositifs() {
        return dispositifRepository.findAll()
                .stream()
                .map(dispositifMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DispositifResponseDTO updateDispositif(int id, DispositifRequestDTO requestDTO) {
        // Récupérer le dispositif existant
        Dispositif dispositif = dispositifRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositif", "id", id));

        // Mettre à jour l'entité
        dispositifMapper.updateEntity(requestDTO, dispositif);

        // Sauvegarder
        Dispositif updatedDispositif = dispositifRepository.save(dispositif);

        return dispositifMapper.toResponseDTO(updatedDispositif);
    }

    @Override
    public void deleteDispositif(int id) {
        // Vérifier que le dispositif existe
        if (!dispositifRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dispositif", "id", id);
        }

        dispositifRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DispositifResponseDTO> searchByNomCentre(String nomCentre) {
        return dispositifRepository.findByNomCentreDeSanteContaining(nomCentre)
                .stream()
                .map(dispositifMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}