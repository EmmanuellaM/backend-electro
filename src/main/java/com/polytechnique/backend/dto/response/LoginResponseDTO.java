package com.polytechnique.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Réponse de connexion unifiée pour admin et médecin")
public class LoginResponseDTO {

    @Schema(description = "ID de l'utilisateur", example = "1")
    private Integer id;

    @Schema(description = "Nom de l'utilisateur", example = "Mbarga")
    private String nom;

    @Schema(description = "Prénom de l'utilisateur", example = "Jean-Pierre")
    private String prenom;

    @Schema(description = "Email de l'utilisateur", example = "jp.mbarga@hopital.cm")
    private String email;

    @Schema(description = "Rôle de l'utilisateur", example = "medecin", allowableValues = { "admin", "medecin" })
    private String role;

    @Schema(description = "Spécialité (médecin uniquement)", example = "Gynécologie-Obstétrique")
    private String specialite;

    @Schema(description = "Téléphone", example = "+237 699 111 222")
    private String telephone;

    @Schema(description = "Statut du compte", example = "actif")
    private String statut;

    @Schema(description = "ID de l'administrateur (si médecin)", example = "1")
    private Integer administrateurId;
}
