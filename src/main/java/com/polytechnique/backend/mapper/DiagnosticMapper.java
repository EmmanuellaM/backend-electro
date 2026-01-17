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
     */
    public Diagnostic toEntity(DiagnosticRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setContenu(dto.getContenu());
        diagnostic.setRecommandations(dto.getRecommandations());
        diagnostic.setNiveauUrgence(dto.getNiveauUrgence());

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
        diagnostic.setRecommandations(dto.getRecommandations());
        diagnostic.setNiveauUrgence(dto.getNiveauUrgence());
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
        dto.setRecommandations(diagnostic.getRecommandations());
        dto.setNiveauUrgence(diagnostic.getNiveauUrgence());
        dto.setDateDiagnostic(diagnostic.getDateDiagnostic());
        dto.setDateValidation(diagnostic.getDateValidation());

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
            dto.setGlycemie(diagnostic.getParametres().getGlycemie());
        }

        return dto;
    }
}