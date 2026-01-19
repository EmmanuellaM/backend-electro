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
        infirmier.setStatut(dto.getStatut() != null ? dto.getStatut() : "actif");
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
            infirmier.setStatut(dto.getStatut());
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
        dto.setStatut(infirmier.getStatut());
        dto.setGenre(infirmier.getGenre());
        return dto;
    }
}
