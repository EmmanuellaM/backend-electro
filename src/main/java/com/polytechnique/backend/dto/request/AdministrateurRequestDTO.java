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

    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    @Schema(description = "Numéro de CNI", example = "109283746")
    private String numeroCni;

    @jakarta.validation.constraints.Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Le format du numéro de téléphone est invalide (doit commencer par +)")
    @Schema(description = "Téléphone principal (format international)", example = "+237600000000")
    private String tel;

    @jakarta.validation.constraints.Pattern(regexp = "^(\\+[1-9]\\d{1,14})?$", message = "Le format du numéro de téléphone secondaire est invalide")
    @Schema(description = "Téléphone secondaire (optionnel)", example = "+237611111111")
    private String tel2;

    @Schema(description = "Genre (MASCULIN / FEMININ)", example = "MASCULIN")
    private String genre;

    private String role; // Optionnel, par défaut ADMIN
}
