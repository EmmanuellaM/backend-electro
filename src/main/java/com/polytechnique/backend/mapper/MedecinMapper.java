package com.polytechnique.backend.mapper;

import com.polytechnique.backend.dto.request.MedecinRequestDTO;
import com.polytechnique.backend.dto.response.MedecinResponseDTO;
import com.polytechnique.backend.entity.Medecin;
import com.polytechnique.backend.entity.StatutMedecin;
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
        medecin.setGenre(dto.getGenre());
        medecin.setSpecialite(dto.getSpecialite());

        // Set password - use provided, otherwise leave null (service will handle
        // generation)
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isBlank()) {
            medecin.setMotDePasse(dto.getMotDePasse());
        }

        // Default status
        medecin.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutMedecin.ACTIF);

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

        // Update genre if provided
        if (dto.getGenre() != null) {
            medecin.setGenre(dto.getGenre());
        }

        // Update specialite if provided
        if (dto.getSpecialite() != null) {
            medecin.setSpecialite(dto.getSpecialite());
        }

        // Update statut if provided
        if (dto.getStatut() != null) {
            medecin.setStatut(dto.getStatut());
        }
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
        dto.setGenre(medecin.getGenre());
        dto.setSpecialite(medecin.getSpecialite());
        dto.setStatut(medecin.getStatut());
        dto.setDateInscription(medecin.getDateInscription());
        dto.setDerniereConnexion(medecin.getDerniereConnexion());

        // Utiliser le champ calculé par @Formula s'il est disponible, sinon 0
        dto.setNombreDiagnostics(
                medecin.getNombreDiagnosticsCalculated() != null ? medecin.getNombreDiagnosticsCalculated() : 0);

        return dto;
    }
}