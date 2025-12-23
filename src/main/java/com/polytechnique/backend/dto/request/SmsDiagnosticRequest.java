package com.polytechnique.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SmsDiagnosticRequest {
    @NotNull(message = "L'ID des paramètres est obligatoire")
    private Integer parametresId;

    @NotNull(message = "L'ID du médecin est obligatoire")
    private Integer medecinId;

    @NotBlank(message = "Le message est obligatoire")
    private String message;

    @NotBlank(message = "Le niveau d'urgence est obligatoire")
    private String niveauUrgence;
}
