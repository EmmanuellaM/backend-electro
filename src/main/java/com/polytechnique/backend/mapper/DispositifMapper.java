package com.polytechnique.backend.mapper;

import com.polytechnique.backend.dto.request.DispositifRequestDTO;
import com.polytechnique.backend.dto.response.DispositifResponseDTO;
import com.polytechnique.backend.dto.response.InfirmierLocalResponseDTO;
import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.entity.InfirmierLocal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Dispositif et ses DTOs
 */
@Component
@RequiredArgsConstructor
public class DispositifMapper {

    private final InfirmierLocalMapper infirmierLocalMapper;

    /**
     * Convertir DispositifRequestDTO → Dispositif (pour création)
     */
    public Dispositif toEntity(DispositifRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Dispositif dispositif = new Dispositif();
        dispositif.setCodeDispositif(dto.getCodeDispositif());
        dispositif.setDeveui(dto.getDeveui());
        dispositif.setNomCentreDeSante(dto.getNomCentreDeSante());
        dispositif.setLocalisation(dto.getLocalisation());
        dispositif.setContact(dto.getContact());
        dispositif.setStatut(dto.getStatut() != null ? dto.getStatut() : "actif");
        dispositif.setDateInstallation(dto.getDateInstallation());

        return dispositif;
    }

    /**
     * Mettre à jour une entité Dispositif existante avec les données du DTO
     */
    public void updateEntity(DispositifRequestDTO dto, Dispositif dispositif) {
        if (dto == null || dispositif == null) {
            return;
        }

        dispositif.setCodeDispositif(dto.getCodeDispositif());
        dispositif.setDeveui(dto.getDeveui());
        dispositif.setNomCentreDeSante(dto.getNomCentreDeSante());
        dispositif.setLocalisation(dto.getLocalisation());
        dispositif.setContact(dto.getContact());
        if (dto.getStatut() != null) {
            dispositif.setStatut(dto.getStatut());
        }
        dispositif.setDateInstallation(dto.getDateInstallation());
    }

    /**
     * Convertir Dispositif → DispositifResponseDTO (pour réponse)
     */
    public DispositifResponseDTO toResponseDTO(Dispositif dispositif) {
        if (dispositif == null) {
            return null;
        }

        DispositifResponseDTO dto = new DispositifResponseDTO();
        dto.setId(dispositif.getId());
        dto.setCodeDispositif(dispositif.getCodeDispositif());
        dto.setDeveui(dispositif.getDeveui());
        dto.setNomCentreDeSante(dispositif.getNomCentreDeSante());
        dto.setLocalisation(dispositif.getLocalisation());
        dto.setContact(dispositif.getContact());
        dto.setStatut(dispositif.getStatut());
        dto.setDateInstallation(dispositif.getDateInstallation());
        dto.setCreatedAt(dispositif.getCreatedAt());

        if (dispositif.getInfirmierLocal() != null) {
            dto.setInfirmierLocal(infirmierLocalMapper.toResponseDTO(dispositif.getInfirmierLocal()));
        }

        // Ajouter le nombre de paramètres si la collection est chargée
        if (dispositif.getParametres() != null) {
            dto.setNombreParametres(dispositif.getParametres().size());
        }

        return dto;
    }
}