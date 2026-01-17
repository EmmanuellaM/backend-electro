package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modèle de création/modification d'un administrateur")
public class AdministrateurRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    @Schema(description = "Nom de l'administrateur", example = "Admin Principal")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    @Size(max = 150)
    @Schema(description = "Adresse email de l'administrateur", example = "admin@hopital.cm")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, max = 255)
    @Schema(description = "Mot de passe sécurisé", example = "AdminSecurePass1!")
    private String motDePasse;
}
