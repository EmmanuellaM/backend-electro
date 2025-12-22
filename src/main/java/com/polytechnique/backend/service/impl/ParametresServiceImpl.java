package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.ParametresRequestDTO;
import com.polytechnique.backend.dto.response.ParametresResponseDTO;
import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.mapper.ParametresMapper;
import com.polytechnique.backend.repository.DispositifRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import com.polytechnique.backend.service.ParametresService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service pour gérer les paramètres médicaux
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ParametresServiceImpl implements ParametresService {

    private final ParametresRepository parametresRepository;
    private final DispositifRepository dispositifRepository;
    private final ParametresMapper parametresMapper;

    @Override
    public ParametresResponseDTO createParametres(ParametresRequestDTO requestDTO) {
        // Récupérer le dispositif
        Dispositif dispositif = dispositifRepository.findById(requestDTO.getDispositifId())
                .orElseThrow(() -> new ResourceNotFoundException("Dispositif", "id", requestDTO.getDispositifId()));

        // Convertir DTO → Entité
        Parametres parametres = parametresMapper.toEntity(requestDTO);
        parametres.setDispositif(dispositif);

        // Sauvegarder
        Parametres savedParametres = parametresRepository.save(parametres);

        // Convertir Entité → DTO de réponse
        return parametresMapper.toResponseDTO(savedParametres);
    }

    @Override
    @Transactional(readOnly = true)
    public ParametresResponseDTO getParametresById(int id) {
        Parametres parametres = parametresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paramètres", "id", id));

        return parametresMapper.toResponseDTO(parametres);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParametresResponseDTO> getAllParametres() {
        return parametresRepository.findAll()
                .stream()
                .map(parametresMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParametresResponseDTO updateParametres(int id, ParametresRequestDTO requestDTO) {
        // Récupérer les paramètres existants
        Parametres parametres = parametresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paramètres", "id", id));

        // Si le dispositif a changé, récupérer le nouveau
        if (requestDTO.getDispositifId() != null &&
                requestDTO.getDispositifId() != parametres.getDispositif().getId()) {
            Dispositif newDispositif = dispositifRepository.findById(requestDTO.getDispositifId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dispositif", "id", requestDTO.getDispositifId()));
            parametres.setDispositif(newDispositif);
        }

        // Mettre à jour l'entité
        parametresMapper.updateEntity(requestDTO, parametres);

        // Sauvegarder
        Parametres updatedParametres = parametresRepository.save(parametres);

        return parametresMapper.toResponseDTO(updatedParametres);
    }

    @Override
    public void deleteParametres(int id) {
        // Vérifier que les paramètres existent
        if (!parametresRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paramètres", "id", id);
        }

        parametresRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParametresResponseDTO> getParametresByPatient(String identifiantPatient) {
        return parametresRepository.findByIdentifiantPatient(identifiantPatient)
                .stream()
                .map(parametresMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParametresResponseDTO> getParametresByDispositif(int dispositifId) {
        // Vérifier que le dispositif existe
        if (!dispositifRepository.existsById(dispositifId)) {
            throw new ResourceNotFoundException("Dispositif", "id", dispositifId);
        }

        return parametresRepository.findByDispositifId(dispositifId)
                .stream()
                .map(parametresMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}