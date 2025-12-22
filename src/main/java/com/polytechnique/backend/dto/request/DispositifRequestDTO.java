package com.polytechnique.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO pour créer ou modifier un dispositif
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispositifRequestDTO {

    @NotBlank(message = "Le code du dispositif est obligatoire")
    @Size(max = 50, message = "Le code ne peut pas dépasser 50 caractères")
    private String codeDispositif;

    @NotBlank(message = "Le nom du centre de santé est obligatoire")
    @Size(max = 150, message = "Le nom du centre ne peut pas dépasser 150 caractères")
    private String nomCentreDeSante;

    @Size(max = 200, message = "La localisation ne peut pas dépasser 200 caractères")
    private String localisation;

    @Size(max = 50, message = "Le contact ne peut pas dépasser 50 caractères")
    private String contact;

    private String statut; // actif, inactif, maintenance

    private LocalDate dateInstallation;
}