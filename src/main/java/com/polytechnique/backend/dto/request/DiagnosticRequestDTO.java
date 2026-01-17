package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modèle de création/modification d'un diagnostic médical")
public class DiagnosticRequestDTO {

    @NotBlank(message = "Le contenu du diagnostic est obligatoire")
    @Schema(description = "Contenu du diagnostic médical", example = "Risque de pré-éclampsie détecté selon les paramètres.")
    private String contenu;

    @NotNull(message = "L'ID du médecin est obligatoire")
    @Schema(description = "ID du médecin établissant le diagnostic", example = "1")
    private Integer medecinId;

    @NotNull(message = "L'ID des paramètres est obligatoire")
    @Schema(description = "ID de l'ensemble de paramètres du patient", example = "10")
    private Integer parametresId;

    @Schema(description = "Recommandations médicales", example = "Surveillance rapprochée + bilan biologique complémentaire.")
    private String recommandations;

    @Schema(description = "Niveau d'urgence", example = "ÉLEVÉ", allowableValues = { "FAIBLE", "MOYEN", "ÉLEVÉ",
            "CRITIQUE" })
    private String niveauUrgence;
}