package com.polytechnique.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la validation d'un diagnostic IA par un médecin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticIAValidationDTO {

    @NotNull(message = "La validation est obligatoire (true/false)")
    private Boolean isValid;

    @NotNull(message = "L'ID du médecin validateur est obligatoire")
    private Integer medecinId;

    private String commentaire;
}
