package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.DispositifRequestDTO;
import com.polytechnique.backend.dto.response.DispositifResponseDTO;
import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.entity.InfirmierLocal;
import com.polytechnique.backend.entity.StatutDispositif;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.mapper.DispositifMapper;
import com.polytechnique.backend.repository.DispositifRepository;
import com.polytechnique.backend.repository.InfirmierLocalRepository;
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
    private final InfirmierLocalRepository infirmierLocalRepository;
    private final com.polytechnique.backend.repository.AdministrateurRepository administrateurRepository;

    @Override
    public DispositifResponseDTO createDispositif(DispositifRequestDTO requestDTO) {
        // Convertir DTO → Entité
        Dispositif dispositif = dispositifMapper.toEntity(requestDTO);

        // Associer l'administrateur
        if (requestDTO.getAdministrateurId() != null) {
            com.polytechnique.backend.entity.Administrateur admin = administrateurRepository
                    .findById(requestDTO.getAdministrateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Administrateur", "id",
                            requestDTO.getAdministrateurId()));
            dispositif.setAdministrateur(admin);
        }

        // Associer l'infirmier si présent
        if (requestDTO.getInfirmierLocalId() != null) {
            InfirmierLocal infirmier = infirmierLocalRepository.findById(requestDTO.getInfirmierLocalId())
                    .orElseThrow(() -> new ResourceNotFoundException("InfirmierLocal", "id",
                            requestDTO.getInfirmierLocalId()));
            dispositif.setInfirmierLocal(infirmier);
        }

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

        // Mettre à jour l'administrateur si nécessaire
        if (requestDTO.getAdministrateurId() != null) {
            com.polytechnique.backend.entity.Administrateur admin = administrateurRepository
                    .findById(requestDTO.getAdministrateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Administrateur", "id",
                            requestDTO.getAdministrateurId()));
            dispositif.setAdministrateur(admin);
        }

        // Mettre à jour la relation infirmier si nécessaire
        if (requestDTO.getInfirmierLocalId() != null) {
            // Si l'ID a changé ou si l'infirmier n'était pas défini
            if (dispositif.getInfirmierLocal() == null
                    || !dispositif.getInfirmierLocal().getId().equals(requestDTO.getInfirmierLocalId())) {
                InfirmierLocal infirmier = infirmierLocalRepository.findById(requestDTO.getInfirmierLocalId())
                        .orElseThrow(() -> new ResourceNotFoundException("InfirmierLocal", "id",
                                requestDTO.getInfirmierLocalId()));
                dispositif.setInfirmierLocal(infirmier);
            }
        } else {
            // Si null dans le DTO, on peut vouloir dissocier (optionnel, ici on garde
            // l'existant ou on dissocie ?)
            // Pour l'instant, si on passe null, on dissocie (ou on ignore ?). Disons qu'on
            // dissocie si c'est explicite.
            // Mais requestDTO.getInfirmierLocalId() == null peut signifier "ne pas
            // changer".
            // Supposons que pour dissocier on doive passer une valeur spécifique ou
            // endpoint spécifique.
            // Simplification: si null, on ne touche pas, sauf si logique spécifique.
        }

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

    @Override
    public DispositifResponseDTO updateStatut(int id, String statut) {
        Dispositif dispositif = dispositifRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositif", "id", id));

        StatutDispositif newStatut = StatutDispositif.valueOf(statut.toUpperCase());
        dispositif.setStatut(newStatut);
        Dispositif updatedDispositif = dispositifRepository.save(dispositif);

        return dispositifMapper.toResponseDTO(updatedDispositif);
    }

    @Override
    public DispositifResponseDTO activerDispositif(int id,
            com.polytechnique.backend.dto.request.ActivationDispositifRequestDTO requestDTO) {
        Dispositif dispositif = dispositifRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositif", "id", id));

        InfirmierLocal infirmier = infirmierLocalRepository.findById(requestDTO.getInfirmierId())
                .orElseThrow(() -> new ResourceNotFoundException("InfirmierLocal", "id", requestDTO.getInfirmierId()));

        dispositif.setInfirmierLocal(infirmier);
        dispositif.setStatut(StatutDispositif.ACTIF); // Passage à ACTIF

        Dispositif savedDispositif = dispositifRepository.save(dispositif);
        return dispositifMapper.toResponseDTO(savedDispositif);
    }
}