package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modèle de création/modification d'un dispositif IoT")
public class DispositifRequestDTO {

    @NotBlank(message = "Le code du dispositif est obligatoire")
    @Size(max = 50, message = "Le code ne peut pas dépasser 50 caractères")
    @Schema(description = "Code unique du dispositif", example = "DISP-YDE-001")
    private String codeDispositif;

    @NotBlank(message = "Le DevEUI est obligatoire")
    @Size(max = 50, message = "Le DevEUI ne peut pas dépasser 50 caractères")
    @Schema(description = "Device EUI (identifiant LoRaWAN unique)", example = "70B3D57ED005A8C1")
    private String deveui;

    @NotBlank(message = "Le nom du centre de santé est obligatoire")
    @Size(max = 150, message = "Le nom du centre ne peut pas dépasser 150 caractères")
    @Schema(description = "Nom du centre de santé", example = "Centre de Santé de Biyem-Assi")
    private String nomCentreDeSante;

    @Size(max = 200, message = "La localisation ne peut pas dépasser 200 caractères")
    @Schema(description = "Localisation géographique du centre", example = "Yaoundé, Quartier Biyem-Assi")
    private String localisation;

    @Size(max = 50, message = "Le contact ne peut pas dépasser 50 caractères")
    @Schema(description = "Contact du centre de santé", example = "+237699111222")
    private String contact;

    @Schema(description = "Statut du dispositif", example = "actif", allowableValues = { "actif", "inactif",
            "maintenance" })
    private String statut;

    @Schema(description = "Date d'installation du dispositif", example = "2024-01-15")
    private LocalDate dateInstallation;

    @Schema(description = "ID de l'infirmier local supervisé", example = "1")
    private Integer infirmierLocalId;

    @Schema(description = "ID de l'administrateur créateur", example = "1")
    private Integer administrateurId;
}