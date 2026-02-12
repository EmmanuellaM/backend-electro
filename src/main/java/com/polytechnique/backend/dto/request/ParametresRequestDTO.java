package com.polytechnique.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO pour créer ou modifier des paramètres médicaux
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametresRequestDTO {

    @NotBlank(message = "L'identifiant du patient est obligatoire")
    @Size(max = 50, message = "L'identifiant du patient ne peut pas dépasser 50 caractères")
    private String identifiantPatient;

    @NotNull(message = "Le poids du patient est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids doit être positif")
    private BigDecimal poidsPatient;

    @NotNull(message = "La température est obligatoire")
    @DecimalMin(value = "30.0", message = "La température doit être >= 30°C")
    private BigDecimal temperature;

    @Min(value = 40, message = "La pression systolique doit être >= 40")
    @Max(value = 300, message = "La pression systolique doit être <= 300")
    private Integer pressionArterielleSystolique;

    @Min(value = 20, message = "La pression diastolique doit être >= 20")
    @Max(value = 200, message = "La pression diastolique doit être <= 200")
    private Integer pressionArterielleDiastolique;

    @Min(value = 50, message = "La fréquence fœtale doit être >= 50")
    @Max(value = 220, message = "La fréquence fœtale doit être <= 220")
    private Integer frequenceFoetale;

    private BigDecimal glycemie;

    private Integer saturationOxygene;

    private LocalDate dateDernieresRegles;

    @NotNull(message = "L'âge du patient est obligatoire")
    @Min(value = 10, message = "L'âge doit être valide")
    @Max(value = 100, message = "L'âge doit être valide")
    private Integer agePatient;

    @NotNull(message = "L'ID du dispositif est obligatoire")
    private Integer dispositifId;
}