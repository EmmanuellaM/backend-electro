package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.polytechnique.backend.status.StatutMedecin;
import com.polytechnique.backend.entity.SpecialiteMedecin;
import com.polytechnique.backend.entity.Genre;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modèle de création/modification d'un médecin")
public class MedecinRequestDTO {

    @NotBlank(message = "Le nom du médecin est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Schema(description = "Nom de famille du médecin", example = "Kamga")
    private String nom;

    @NotBlank(message = "Le prénom du médecin est obligatoire")
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    @Schema(description = "Prénom du médecin", example = "Paul")
    private String prenom;

    @NotBlank(message = "L'email du médecin est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 150, message = "L'email ne peut pas dépasser 150 caractères")
    @Schema(description = "Adresse email professionnelle", example = "dr.kamga@hopital.cm")
    private String email;

    @jakarta.validation.constraints.Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Le format du numéro de téléphone est invalide (doit commencer par +)")
    @Schema(description = "Numéro de téléphone", example = "+237677123456")
    private String tel;

    @NotBlank(message = "Le numéro de CNI est obligatoire")
    @Size(max = 50, message = "Le numéro de CNI ne peut pas dépasser 50 caractères")
    @Schema(description = "Numéro de carte nationale d'identité", example = "CM-123456789")
    private String numeroCarteIdentite;

    @Schema(description = "Mot de passe (optionnel, pour création manuelle via API)", example = "SecurePass123!")
    private String motDePasse;

    @Schema(description = "Genre du médecin", example = "MASCULIN")
    private Genre genre;

    @Schema(description = "Spécialité médicale", example = "GYNECOLOGIE_OBSTETRIQUE")
    private SpecialiteMedecin specialite;

    @Schema(description = "Statut du médecin (ACTIF, INACTIF, SUSPENDU)", example = "ACTIF")
    private StatutMedecin statut;

    @Schema(description = "ID de l'administrateur créateur", example = "1")
    private Integer administrateurId;
}