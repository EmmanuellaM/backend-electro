package com.polytechnique.backend.dto.request;

import com.polytechnique.backend.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Modèle de création/modification d'un infirmier local")
public class InfirmierLocalRequestDTO {
    @NotBlank
    @Schema(description = "Nom de l'infirmier", example = "Dupont")
    private String nom;
    @NotBlank
    @Schema(description = "Prénom de l'infirmier", example = "Jean")
    private String prenom;
    @NotBlank
    @Schema(description = "Numéro de téléphone principal", example = "+237699000001")
    private String telephone1;

    @Schema(description = "Numéro de téléphone secondaire (optionnel)", example = "+237677000002")
    private String telephone2;
    @NotBlank
    @Schema(description = "Zone géographique d'affectation", example = "Yaoundé - Biyem-Assi")
    private String zoneAffectation;

    @Schema(description = "Statut de l'infirmier", example = "actif", defaultValue = "actif")
    private String statut;

    @Schema(description = "Genre de l'infirmier", example = "MASCULIN")
    private Genre genre;

    @Schema(description = "ID de l'administrateur créateur", example = "1")
    private Integer administrateurId;
}
