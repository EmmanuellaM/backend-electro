package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Modèle pour l'envoi manuel d'une notification SMS")
public class NotificationSMSRequestDTO {
    @NotBlank
    @Schema(description = "Contenu du message SMS", example = "Alerte: Patient DISP-001 en situation critique.")
    private String contenuMessage;

    @NotBlank
    @Schema(description = "Numéro de téléphone du destinataire", example = "+237699000001")
    private String numeroDestinataire;

    @NotNull
    @Schema(description = "ID de l'infirmier destinataire", example = "1")
    private Integer infirmierId;

    @Schema(description = "ID du diagnostic associé (optionnel)", example = "10")
    private Integer diagnosticId;

    @Schema(description = "Statut de l'envoi (simulation)", example = "true", defaultValue = "true")
    private Boolean succes;
}
