package com.polytechnique.backend.dto.response;

import com.polytechnique.backend.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Informations d'un infirmier local")
public class InfirmierLocalResponseDTO {
    @Schema(description = "Identifiant unique", example = "1")
    private Integer id;

    @Schema(description = "Nom de famille", example = "Dupont")
    private String nom;

    @Schema(description = "Prénom", example = "Jean")
    private String prenom;

    @Schema(description = "Téléphone principal", example = "+237699000001")
    private String telephone1;

    @Schema(description = "Téléphone secondaire", example = "+237677000002")
    private String telephone2;

    @Schema(description = "Zone d'affectation", example = "Yaoundé - Biyem-Assi")
    private String zoneAffectation;

    @Schema(description = "Statut de l'infirmier", example = "actif")
    private String statut;

    @Schema(description = "Genre de l'infirmier", example = "MASCULIN")
    private Genre genre;
}
