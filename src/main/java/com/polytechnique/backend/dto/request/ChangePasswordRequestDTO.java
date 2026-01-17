package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Modèle de changement de mot de passe")
public class ChangePasswordRequestDTO {

    @NotBlank(message = "L'ancien mot de passe est obligatoire")
    @Schema(description = "Mot de passe actuel", example = "oldPassword123")
    private String ancienMotDePasse;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 6, message = "Le nouveau mot de passe doit contenir au moins 6 caractères")
    @Schema(description = "Nouveau mot de passe souhaité", example = "newSecurePass!")
    private String nouveauMotDePasse;
}
