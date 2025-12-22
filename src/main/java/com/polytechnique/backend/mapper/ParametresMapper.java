package com.polytechnique.backend.mapper;

import com.polytechnique.backend.dto.request.ParametresRequestDTO;
import com.polytechnique.backend.dto.response.ParametresResponseDTO;
import com.polytechnique.backend.entity.Parametres;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Parametres et ses DTOs
 */
@Component
public class ParametresMapper {

    /**
     * Convertir ParametresRequestDTO → Parametres (pour création)
     * Note: Le dispositif doit être récupéré et assigné par le service
     */
    public Parametres toEntity(ParametresRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Parametres parametres = new Parametres();
        parametres.setIdentifiantPatient(dto.getIdentifiantPatient());
        parametres.setPoidsPatient(dto.getPoidsPatient());
        parametres.setTemperature(dto.getTemperature());
        parametres.setPressionArterielleSystolique(dto.getPressionArterielleSystolique());
        parametres.setPressionArterielleDiastolique(dto.getPressionArterielleDiastolique());
        parametres.setFrequenceFoetale(dto.getFrequenceFoetale());
        
        return parametres;
    }

    /**
     * Mettre à jour une entité Parametres existante avec les données du DTO
     */
    public void updateEntity(ParametresRequestDTO dto, Parametres parametres) {
        if (dto == null || parametres == null) {
            return;
        }

        parametres.setIdentifiantPatient(dto.getIdentifiantPatient());
        parametres.setPoidsPatient(dto.getPoidsPatient());
        parametres.setTemperature(dto.getTemperature());
        parametres.setPressionArterielleSystolique(dto.getPressionArterielleSystolique());
        parametres.setPressionArterielleDiastolique(dto.getPressionArterielleDiastolique());
        parametres.setFrequenceFoetale(dto.getFrequenceFoetale());
        // Note: Le dispositif doit être mis à jour par le service si nécessaire
    }

    /**
     * Convertir Parametres → ParametresResponseDTO (pour réponse)
     */
    public ParametresResponseDTO toResponseDTO(Parametres parametres) {
        if (parametres == null) {
            return null;
        }

        ParametresResponseDTO dto = new ParametresResponseDTO();
        dto.setId(parametres.getId());
        dto.setIdentifiantPatient(parametres.getIdentifiantPatient());
        dto.setPoidsPatient(parametres.getPoidsPatient());
        dto.setTemperature(parametres.getTemperature());
        dto.setPressionArterielleSystolique(parametres.getPressionArterielleSystolique());
        dto.setPressionArterielleDiastolique(parametres.getPressionArterielleDiastolique());
        dto.setFrequenceFoetale(parametres.getFrequenceFoetale());
        dto.setDateMesure(parametres.getDateMesure());
        dto.setStatut(parametres.getStatut());
        
        // Ajouter les informations du dispositif
        if (parametres.getDispositif() != null) {
            dto.setDispositifId(parametres.getDispositif().getId());
            dto.setNomCentreDeSante(parametres.getDispositif().getNomCentreDeSante());
            dto.setCodeDispositif(parametres.getDispositif().getCodeDispositif());
        }
        
        // Ajouter le nombre de diagnostics si la collection est chargée
        if (parametres.getDiagnostics() != null) {
            dto.setNombreDiagnostics(parametres.getDiagnostics().size());
        }
        
        return dto;
    }
}