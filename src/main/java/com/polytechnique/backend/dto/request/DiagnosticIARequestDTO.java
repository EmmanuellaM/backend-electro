package com.polytechnique.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la requête de création d'un diagnostic IA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticIARequestDTO {

    @NotNull(message = "L'ID des paramètres est obligatoire")
    private Integer parametresId;

    private Boolean includeExplanation = true;
}
