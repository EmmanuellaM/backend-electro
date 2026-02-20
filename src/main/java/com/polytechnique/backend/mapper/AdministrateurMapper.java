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
        admin.setNumeroCni(dto.getNumeroCni());
        admin.setTel(dto.getTel());
        admin.setTel2(dto.getTel2());
        if (dto.getGenre() != null) {
            try {
                admin.setGenre(com.polytechnique.backend.entity.Genre.valueOf(dto.getGenre()));
            } catch (IllegalArgumentException e) {
                // Ignore invalid genre
            }
        }
        try {
            if (dto.getRole() != null) {
                admin.setRole(com.polytechnique.backend.entity.Role.valueOf(dto.getRole()));
            }
        } catch (IllegalArgumentException e) {
            // Ignore invalid roles, default is ADMIN
        }

        if (dto.getPatientLockTimeout() != null) {
            admin.setPatientLockTimeout(dto.getPatientLockTimeout());
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
        dto.setNumeroCni(admin.getNumeroCni());
        dto.setTel(admin.getTel());
        dto.setTel2(admin.getTel2());
        dto.setGenre(admin.getGenre() != null ? admin.getGenre().name() : null);
        dto.setDoitChangerMotDePasse(admin.getDoitChangerMotDePasse());
        dto.setPatientLockTimeout(admin.getPatientLockTimeout());
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
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty())
            admin.setMotDePasse(dto.getMotDePasse());
        if (dto.getNumeroCni() != null)
            admin.setNumeroCni(dto.getNumeroCni());
        if (dto.getTel() != null)
            admin.setTel(dto.getTel());
        if (dto.getTel2() != null)
            admin.setTel2(dto.getTel2());
        if (dto.getGenre() != null) {
            try {
                admin.setGenre(com.polytechnique.backend.entity.Genre.valueOf(dto.getGenre()));
            } catch (IllegalArgumentException e) {
                // Ignore
            }
        }
        if (dto.getRole() != null) {
            try {
                admin.setRole(com.polytechnique.backend.entity.Role.valueOf(dto.getRole()));
            } catch (IllegalArgumentException e) {
                // Ignore invalid role updates
            }
        }
        if (dto.getPatientLockTimeout() != null) {
            admin.setPatientLockTimeout(dto.getPatientLockTimeout());
        }
    }
}
