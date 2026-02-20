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
    private final com.polytechnique.backend.repository.DispositifRepository dispositifRepository;

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
        dto.setFrequenceCardiaqueMereMin(entity.getFrequenceCardiaqueMereMin());
        dto.setFrequenceCardiaqueMereMax(entity.getFrequenceCardiaqueMereMax());
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
        seuil.setFrequenceCardiaqueMereMin(requestDTO.getFrequenceCardiaqueMereMin());
        seuil.setFrequenceCardiaqueMereMax(requestDTO.getFrequenceCardiaqueMereMax());
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
        seuil.setFrequenceCardiaqueMereMin(60);
        seuil.setFrequenceCardiaqueMereMax(100);
        seuil.setPressionSystoliqueMax(140);
        seuil.setPressionDiastoliqueMax(90);
        seuil.setGlycemieMax(new BigDecimal("7.0"));
        seuil.setSaturationOxygeneMin(95);
        return toDTO(seuilMaintenanceRepository.save(seuil));
    }

    @Override
    @Transactional
    public void checkMaintenance(com.polytechnique.backend.entity.Parametres p) {
        if (p == null || p.getDispositif() == null)
            return;

        SeuilMaintenance s = getOrCreateSeuil();
        boolean maintenanceRequise = false;

        // Temperature
        if (p.getTemperature() != null) {
            if (p.getTemperature().compareTo(s.getTemperatureMin()) < 0 ||
                    p.getTemperature().compareTo(s.getTemperatureMax()) > 0) {
                maintenanceRequise = true;
            }
        }

        // FCF
        if (p.getFrequenceFoetale() != null) {
            if (p.getFrequenceFoetale() < s.getFrequenceFoetaleMin() ||
                    p.getFrequenceFoetale() > s.getFrequenceFoetaleMax()) {
                maintenanceRequise = true;
            }
        }

        // FCM (Mère)
        if (p.getFrequenceCardiaqueMere() != null) {
            if (p.getFrequenceCardiaqueMere() < s.getFrequenceCardiaqueMereMin() ||
                    p.getFrequenceCardiaqueMere() > s.getFrequenceCardiaqueMereMax()) {
                maintenanceRequise = true;
            }
        }

        // Pression
        if (p.getPressionArterielleSystolique() != null &&
                p.getPressionArterielleSystolique() > s.getPressionSystoliqueMax()) {
            maintenanceRequise = true;
        }
        if (p.getPressionArterielleDiastolique() != null &&
                p.getPressionArterielleDiastolique() > s.getPressionDiastoliqueMax()) {
            maintenanceRequise = true;
        }

        // Glycémie
        if (p.getGlycemie() != null && s.getGlycemieMax() != null &&
                p.getGlycemie().compareTo(s.getGlycemieMax()) > 0) {
            maintenanceRequise = true;
        }

        // SpO2
        if (p.getSaturationOxygene() != null &&
                p.getSaturationOxygene() < s.getSaturationOxygeneMin()) {
            maintenanceRequise = true;
        }

        if (maintenanceRequise) {
            com.polytechnique.backend.entity.Dispositif d = p.getDispositif();
            // Eviter les mises à jour inutiles si déjà en maintenance
            if (d.getStatut() != com.polytechnique.backend.status.StatutDispositif.MAINTENANCE) {
                d.setStatut(com.polytechnique.backend.status.StatutDispositif.MAINTENANCE);
                dispositifRepository.save(d);
            }
        }
    }
}
