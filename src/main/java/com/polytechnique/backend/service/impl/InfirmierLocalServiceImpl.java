package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.InfirmierLocalRequestDTO;
import com.polytechnique.backend.dto.response.InfirmierLocalResponseDTO;
import com.polytechnique.backend.entity.InfirmierLocal;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.mapper.InfirmierLocalMapper;
import com.polytechnique.backend.repository.InfirmierLocalRepository;
import com.polytechnique.backend.service.InfirmierLocalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InfirmierLocalServiceImpl implements InfirmierLocalService {

    private final InfirmierLocalRepository infirmierLocalRepository;
    private final InfirmierLocalMapper infirmierLocalMapper;
    private final com.polytechnique.backend.repository.AdministrateurRepository administrateurRepository;

    @Override
    public InfirmierLocalResponseDTO createInfirmier(InfirmierLocalRequestDTO requestDTO) {
        InfirmierLocal infirmier = infirmierLocalMapper.toEntity(requestDTO);

        if (requestDTO.getAdministrateurId() != null) {
            com.polytechnique.backend.entity.Administrateur admin = administrateurRepository
                    .findById(requestDTO.getAdministrateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Administrateur", "id",
                            requestDTO.getAdministrateurId()));
            infirmier.setAdministrateur(admin);
        }

        InfirmierLocal savedInfirmier = infirmierLocalRepository.save(infirmier);
        return infirmierLocalMapper.toResponseDTO(savedInfirmier);
    }

    @Override
    @Transactional(readOnly = true)
    public InfirmierLocalResponseDTO getInfirmierById(int id) {
        InfirmierLocal infirmier = infirmierLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Infirmier", "id", id));
        return infirmierLocalMapper.toResponseDTO(infirmier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfirmierLocalResponseDTO> getAllInfirmiers(Integer adminId) {
        List<InfirmierLocal> infirmiers;
        if (adminId != null) {
            infirmiers = infirmierLocalRepository.findByAdministrateurId(adminId);
        } else {
            infirmiers = infirmierLocalRepository.findAll();
        }
        return infirmiers.stream()
                .map(infirmierLocalMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InfirmierLocalResponseDTO updateInfirmier(int id, InfirmierLocalRequestDTO requestDTO) {
        InfirmierLocal infirmier = infirmierLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Infirmier", "id", id));

        String oldPhone = infirmier.getTelephone1();
        infirmierLocalMapper.updateEntity(requestDTO, infirmier);
        String newPhone = infirmier.getTelephone1();

        // Si le téléphone a changé, mettre à jour le contact des dispositifs associés
        if (oldPhone != null && !oldPhone.equals(newPhone)) {
            if (infirmier.getDispositifs() != null) {
                for (com.polytechnique.backend.entity.Dispositif d : infirmier.getDispositifs()) {
                    d.setContact(newPhone);
                }
            }
        }

        if (requestDTO.getAdministrateurId() != null) {
            com.polytechnique.backend.entity.Administrateur admin = administrateurRepository
                    .findById(requestDTO.getAdministrateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Administrateur", "id",
                            requestDTO.getAdministrateurId()));
            infirmier.setAdministrateur(admin);
        }

        InfirmierLocal updatedInfirmier = infirmierLocalRepository.save(infirmier);

        return infirmierLocalMapper.toResponseDTO(updatedInfirmier);
    }

    @Override
    public void deleteInfirmier(int id) {
        if (!infirmierLocalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Infirmier", "id", id);
        }
        infirmierLocalRepository.deleteById(id);
    }

    @Override
    public InfirmierLocalResponseDTO updateStatut(int id, String statut) {
        InfirmierLocal infirmier = infirmierLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Infirmier", "id", id));
        infirmier.setStatut(statut != null ? statut.toLowerCase() : "actif");
        InfirmierLocal updatedInfirmier = infirmierLocalRepository.save(infirmier);
        return infirmierLocalMapper.toResponseDTO(updatedInfirmier);
    }
}
