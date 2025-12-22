package com.polytechnique.backend.mapper;

import com.polytechnique.backend.dto.request.MedecinRequestDTO;
import com.polytechnique.backend.dto.response.MedecinResponseDTO;
import com.polytechnique.backend.entity.Medecin;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Medecin et ses DTOs
 */
@Component
public class MedecinMapper {

    /**
     * Convertir MedecinRequestDTO → Medecin (pour création)
     */
    public Medecin toEntity(MedecinRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Medecin medecin = new Medecin();
        medecin.setNom(dto.getNom());
        medecin.setPrenom(dto.getPrenom());
        medecin.setEmail(dto.getEmail());
        medecin.setTel(dto.getTel());
        
        return medecin;
    }

    /**
     * Mettre à jour une entité Medecin existante avec les données du DTO
     */
    public void updateEntity(MedecinRequestDTO dto, Medecin medecin) {
        if (dto == null || medecin == null) {
            return;
        }

        medecin.setNom(dto.getNom());
        medecin.setPrenom(dto.getPrenom());
        medecin.setEmail(dto.getEmail());
        medecin.setTel(dto.getTel());
    }

    /**
     * Convertir Medecin → MedecinResponseDTO (pour réponse)
     */
    public MedecinResponseDTO toResponseDTO(Medecin medecin) {
        if (medecin == null) {
            return null;
        }

        MedecinResponseDTO dto = new MedecinResponseDTO();
        dto.setId(medecin.getId());
        dto.setNom(medecin.getNom());
        dto.setPrenom(medecin.getPrenom());
        dto.setEmail(medecin.getEmail());
        dto.setTel(medecin.getTel());
        
        // Ajouter le nombre de diagnostics si la collection est chargée
        if (medecin.getDiagnostics() != null) {
            dto.setNombreDiagnostics(medecin.getDiagnostics().size());
        }
        
        return dto;
    }
}