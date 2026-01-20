package com.polytechnique.backend.mapper;

import com.polytechnique.backend.dto.request.AdministrateurRequestDTO;
import com.polytechnique.backend.dto.response.AdministrateurResponseDTO;
import com.polytechnique.backend.entity.Administrateur;
import org.springframework.stereotype.Component;

@Component
public class AdministrateurMapper {

    public Administrateur toEntity(AdministrateurRequestDTO dto) {
        if (dto == null)
            return null;

        Administrateur admin = new Administrateur();
        admin.setNom(dto.getNom());
        admin.setEmail(dto.getEmail());
        admin.setMotDePasse(dto.getMotDePasse());
        try {
            if (dto.getRole() != null) {
                admin.setRole(com.polytechnique.backend.entity.Role.valueOf(dto.getRole()));
            }
        } catch (IllegalArgumentException e) {
            // Ignore invalid roles, default is ADMIN
        }

        return admin;
    }

    public AdministrateurResponseDTO toResponseDTO(Administrateur admin) {
        if (admin == null)
            return null;

        AdministrateurResponseDTO dto = new AdministrateurResponseDTO();
        dto.setId(admin.getId());
        dto.setNom(admin.getNom());
        dto.setEmail(admin.getEmail());
        dto.setRole(admin.getRole().name());
        dto.setStatut(admin.getStatut() != null ? admin.getStatut().name() : "ACTIF");
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setUpdatedAt(admin.getUpdatedAt());

        return dto;
    }

    public void updateEntity(AdministrateurRequestDTO dto, Administrateur admin) {
        if (dto == null || admin == null)
            return;

        if (dto.getNom() != null)
            admin.setNom(dto.getNom());
        if (dto.getEmail() != null)
            admin.setEmail(dto.getEmail());
        if (dto.getMotDePasse() != null)
            admin.setMotDePasse(dto.getMotDePasse());
        if (dto.getRole() != null) {
            try {
                admin.setRole(com.polytechnique.backend.entity.Role.valueOf(dto.getRole()));
            } catch (IllegalArgumentException e) {
                // Ignore invalid role updates
            }
        }
    }
}
