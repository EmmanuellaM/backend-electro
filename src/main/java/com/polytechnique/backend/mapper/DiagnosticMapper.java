package com.polytechnique.backend.mapper;

import com.polytechnique.backend.dto.request.DiagnosticRequestDTO;
import com.polytechnique.backend.dto.response.DiagnosticResponseDTO;
import com.polytechnique.backend.entity.Diagnostic;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Diagnostic et ses DTOs
 */
@Component
public class DiagnosticMapper {

    /**
     * Convertir DiagnosticRequestDTO → Diagnostic (pour création)
     * Note: Le médecin et les paramètres doivent être récupérés et assignés par le service
     */
    public Diagnostic toEntity(DiagnosticRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setContenu(dto.getContenu());
        
        return diagnostic;
    }

    /**
     * Mettre à jour une entité Diagnostic existante avec les données du DTO
     */
    public void updateEntity(DiagnosticRequestDTO dto, Diagnostic diagnostic) {
        if (dto == null || diagnostic == null) {
            return;
        }

        diagnostic.setContenu(dto.getContenu());
        // Note: Le médecin et les paramètres doivent être mis à jour par le service si nécessaire
    }

    /**
     * Convertir Diagnostic → DiagnosticResponseDTO (pour réponse)
     */
    public DiagnosticResponseDTO toResponseDTO(Diagnostic diagnostic) {
        if (diagnostic == null) {
            return null;
        }

        DiagnosticResponseDTO dto = new DiagnosticResponseDTO();
        dto.setId(diagnostic.getId());
        dto.setContenu(diagnostic.getContenu());
        dto.setDateDiagnostic(diagnostic.getDateDiagnostic());
        
        // Ajouter les informations du médecin
        if (diagnostic.getMedecin() != null) {
            dto.setMedecinId(diagnostic.getMedecin().getId());
            dto.setMedecinNom(diagnostic.getMedecin().getNom());
            dto.setMedecinPrenom(diagnostic.getMedecin().getPrenom());
            dto.setMedecinEmail(diagnostic.getMedecin().getEmail());
        }
        
        // Ajouter les informations des paramètres du patient
        if (diagnostic.getParametres() != null) {
            dto.setParametresId(diagnostic.getParametres().getId());
            dto.setIdentifiantPatient(diagnostic.getParametres().getIdentifiantPatient());
            dto.setPoidsPatient(diagnostic.getParametres().getPoidsPatient());
            dto.setTemperature(diagnostic.getParametres().getTemperature());
            dto.setPressionArterielleSystolique(diagnostic.getParametres().getPressionArterielleSystolique());
            dto.setPressionArterielleDiastolique(diagnostic.getParametres().getPressionArterielleDiastolique());
            dto.setFrequenceFoetale(diagnostic.getParametres().getFrequenceFoetale());
            dto.setDateMesure(diagnostic.getParametres().getDateMesure());
        }
        
        return dto;
    }
}