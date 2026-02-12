package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.SeuilMaintenanceRequestDTO;
import com.polytechnique.backend.dto.response.SeuilMaintenanceResponseDTO;
import com.polytechnique.backend.entity.SeuilMaintenance;
import com.polytechnique.backend.repository.SeuilMaintenanceRepository;
import com.polytechnique.backend.service.SeuilMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SeuilMaintenanceServiceImpl implements SeuilMaintenanceService {

    private final SeuilMaintenanceRepository seuilMaintenanceRepository;

    /**
     * Récupère l'unique enregistrement de seuils (id=1).
     * S'il n'existe pas encore, il est créé avec les valeurs par défaut.
     */
    private SeuilMaintenance getOrCreateSeuil() {
        return seuilMaintenanceRepository.findById(1)
                .orElseGet(() -> {
                    SeuilMaintenance defaults = new SeuilMaintenance();
                    defaults.setId(1);
                    return seuilMaintenanceRepository.save(defaults);
                });
    }

    private SeuilMaintenanceResponseDTO toDTO(SeuilMaintenance entity) {
        SeuilMaintenanceResponseDTO dto = new SeuilMaintenanceResponseDTO();
        dto.setTemperatureMin(entity.getTemperatureMin());
        dto.setTemperatureMax(entity.getTemperatureMax());
        dto.setFrequenceFoetaleMin(entity.getFrequenceFoetaleMin());
        dto.setFrequenceFoetaleMax(entity.getFrequenceFoetaleMax());
        dto.setPressionSystoliqueMax(entity.getPressionSystoliqueMax());
        dto.setPressionDiastoliqueMax(entity.getPressionDiastoliqueMax());
        dto.setGlycemieMax(entity.getGlycemieMax());
        dto.setSaturationOxygeneMin(entity.getSaturationOxygeneMin());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public SeuilMaintenanceResponseDTO getSeuils() {
        return toDTO(getOrCreateSeuil());
    }

    @Override
    @Transactional
    public SeuilMaintenanceResponseDTO updateSeuils(SeuilMaintenanceRequestDTO requestDTO) {
        SeuilMaintenance seuil = getOrCreateSeuil();
        seuil.setTemperatureMin(requestDTO.getTemperatureMin());
        seuil.setTemperatureMax(requestDTO.getTemperatureMax());
        seuil.setFrequenceFoetaleMin(requestDTO.getFrequenceFoetaleMin());
        seuil.setFrequenceFoetaleMax(requestDTO.getFrequenceFoetaleMax());
        seuil.setPressionSystoliqueMax(requestDTO.getPressionSystoliqueMax());
        seuil.setPressionDiastoliqueMax(requestDTO.getPressionDiastoliqueMax());
        seuil.setGlycemieMax(requestDTO.getGlycemieMax());
        seuil.setSaturationOxygeneMin(requestDTO.getSaturationOxygeneMin());
        return toDTO(seuilMaintenanceRepository.save(seuil));
    }

    @Override
    @Transactional
    public SeuilMaintenanceResponseDTO resetSeuils() {
        SeuilMaintenance seuil = getOrCreateSeuil();
        seuil.setTemperatureMin(new BigDecimal("35.5"));
        seuil.setTemperatureMax(new BigDecimal("38.5"));
        seuil.setFrequenceFoetaleMin(110);
        seuil.setFrequenceFoetaleMax(160);
        seuil.setPressionSystoliqueMax(140);
        seuil.setPressionDiastoliqueMax(90);
        seuil.setGlycemieMax(new BigDecimal("7.0"));
        seuil.setSaturationOxygeneMin(95);
        return toDTO(seuilMaintenanceRepository.save(seuil));
    }
}
