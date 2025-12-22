package com.polytechnique.backend.mapper;

import com.polytechnique.backend.dto.request.DispositifRequestDTO;
import com.polytechnique.backend.dto.response.DispositifResponseDTO;
import com.polytechnique.backend.entity.Dispositif;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Dispositif et ses DTOs
 */
@Component
public class DispositifMapper {

    /**
     * Convertir DispositifRequestDTO → Dispositif (pour création)
     */
    public Dispositif toEntity(DispositifRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Dispositif dispositif = new Dispositif();
        dispositif.setCodeDispositif(dto.getCodeDispositif());
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
        dto.setNomCentreDeSante(dispositif.getNomCentreDeSante());
        dto.setLocalisation(dispositif.getLocalisation());
        dto.setContact(dispositif.getContact());
        dto.setStatut(dispositif.getStatut());
        dto.setDateInstallation(dispositif.getDateInstallation());
        dto.setCreatedAt(dispositif.getCreatedAt());
        
        // Ajouter le nombre de paramètres si la collection est chargée
        if (dispositif.getParametres() != null) {
            dto.setNombreParametres(dispositif.getParametres().size());
        }
        
        return dto;
    }
}