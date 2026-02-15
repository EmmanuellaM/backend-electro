package com.polytechnique.backend.mapper;

import com.polytechnique.backend.dto.request.InfirmierLocalRequestDTO;
import com.polytechnique.backend.dto.response.InfirmierLocalResponseDTO;
import com.polytechnique.backend.entity.InfirmierLocal;
import org.springframework.stereotype.Component;

@Component
public class InfirmierLocalMapper {

    public InfirmierLocal toEntity(InfirmierLocalRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        InfirmierLocal infirmier = new InfirmierLocal();
        infirmier.setNom(dto.getNom());
        infirmier.setPrenom(dto.getPrenom());
        infirmier.setTelephone1(dto.getTelephone1());
        infirmier.setTelephone2(dto.getTelephone2());
        infirmier.setZoneAffectation(dto.getZoneAffectation());
        if (dto.getStatut() != null) {
            try {
                infirmier.setStatut(
                        com.polytechnique.backend.status.StatutInfirmier.valueOf(dto.getStatut().toUpperCase()));
            } catch (IllegalArgumentException e) {
                infirmier.setStatut(com.polytechnique.backend.status.StatutInfirmier.ACTIF);
            }
        } else {
            infirmier.setStatut(com.polytechnique.backend.status.StatutInfirmier.ACTIF);
        }
        infirmier.setGenre(dto.getGenre());
        return infirmier;
    }

    public void updateEntity(InfirmierLocalRequestDTO dto, InfirmierLocal infirmier) {
        if (dto == null || infirmier == null) {
            return;
        }
        infirmier.setNom(dto.getNom());
        infirmier.setPrenom(dto.getPrenom());
        infirmier.setTelephone1(dto.getTelephone1());
        infirmier.setTelephone2(dto.getTelephone2());
        infirmier.setZoneAffectation(dto.getZoneAffectation());
        if (dto.getStatut() != null) {
            try {
                infirmier.setStatut(
                        com.polytechnique.backend.status.StatutInfirmier.valueOf(dto.getStatut().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Ignore invalid status in update
            }
        }
        if (dto.getGenre() != null) {
            infirmier.setGenre(dto.getGenre());
        }
    }

    public InfirmierLocalResponseDTO toResponseDTO(InfirmierLocal infirmier) {
        if (infirmier == null) {
            return null;
        }
        InfirmierLocalResponseDTO dto = new InfirmierLocalResponseDTO();
        dto.setId(infirmier.getId());
        dto.setNom(infirmier.getNom());
        dto.setPrenom(infirmier.getPrenom());
        dto.setTelephone1(infirmier.getTelephone1());
        dto.setTelephone2(infirmier.getTelephone2());
        dto.setZoneAffectation(infirmier.getZoneAffectation());
        dto.setStatut(infirmier.getStatut() != null ? infirmier.getStatut().name().toLowerCase() : null);
        dto.setGenre(infirmier.getGenre());
        return dto;
    }
}
