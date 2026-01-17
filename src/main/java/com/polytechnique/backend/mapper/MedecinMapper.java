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
        medecin.setNumeroCarteIdentite(dto.getNumeroCarteIdentite());

        // Set password - use provided or generate default
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isBlank()) {
            medecin.setMotDePasse(dto.getMotDePasse());
        } else {
            // Default password: changeme123 (should be changed on first login)
            medecin.setMotDePasse("changeme123");
        }

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
        medecin.setNumeroCarteIdentite(dto.getNumeroCarteIdentite());
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
        dto.setNumeroCarteIdentite(medecin.getNumeroCarteIdentite());
        dto.setStatut(medecin.getStatut());
        dto.setDateInscription(medecin.getDateInscription());
        dto.setDerniereConnexion(medecin.getDerniereConnexion());

        // Ajouter le nombre de diagnostics si la collection est chargée
        if (medecin.getDiagnostics() != null) {
            dto.setNombreDiagnostics(medecin.getDiagnostics().size());
        }

        return dto;
    }
}