package com.polytechnique.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour créer ou modifier un diagnostic
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticRequestDTO {

    @NotBlank(message = "Le contenu du diagnostic est obligatoire")
    private String contenu;

    @NotNull(message = "L'ID du médecin est obligatoire")
    private Integer medecinId;

    @NotNull(message = "L'ID des paramètres est obligatoire")
    private Integer parametresId;
}