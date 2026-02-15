package com.polytechnique.backend.dto.response;

import com.polytechnique.backend.status.StatutParametre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Réponse représentant un patient avec ses derniers paramètres")
public class PatientResponseDTO {

    @Schema(description = "ID unique (basé sur le dernier paramètre)", example = "1")
    private Integer id;

    @Schema(description = "Identifiant unique du patient", example = "DISP-YDE-001-P01")
    private String identifiantPatient;

    @Schema(description = "Âge du patient", example = "28")
    private Integer age;

    @Schema(description = "Semaine de grossesse actuelle", example = "32")
    private Integer semaineGrossesse;

    @Schema(description = "Numéro de téléphone du patient", example = "+237 699 001 001")
    private String telephone;

    @Schema(description = "Statut du patient", example = "en_attente")
    private StatutParametre statut;

    @Schema(description = "Date de la dernière mesure")
    private LocalDateTime dateDerniereMesure;

    // Derniers paramètres médicaux
    @Schema(description = "Dernier poids mesuré en kg", example = "62.5")
    private BigDecimal poidsPatient;

    @Schema(description = "Dernière taille mesurée en cm", example = "165.0")
    private BigDecimal taillePatient;

    @Schema(description = "Dernière température en °C", example = "36.8")
    private BigDecimal temperature;

    @Schema(description = "Dernière pression systolique en mmHg", example = "120")
    private Integer pressionArterielleSystolique;

    @Schema(description = "Dernière pression diastolique en mmHg", example = "75")
    private Integer pressionArterielleDiastolique;

    @Schema(description = "Dernière fréquence fœtale en bpm", example = "142")
    private Integer frequenceFoetale;

    @Schema(description = "Dernière glycémie en mmol/L", example = "5.2")
    private BigDecimal glycemie;

    @Schema(description = "Dernière saturation en oxygène en %", example = "97")
    private Integer saturationOxygene;

    @Schema(description = "Date des dernières règles", example = "2025-09-15")
    private LocalDate dateDernieresRegles;

    // Informations sur le dispositif
    @Schema(description = "ID du dispositif")
    private Integer dispositifId;

    @Schema(description = "Nom du centre de santé", example = "Centre de Santé de Yaoundé")
    private String nomCentreDeSante;

    @Schema(description = "Localisation du centre", example = "Yaoundé, Mfoundi")
    private String localisation;

    @Schema(description = "ID du médecin qui a verrouillé le patient", example = "1")
    private Integer verrouilleParMedecinId;

    @Schema(description = "Nom du médecin qui a verrouillé le patient", example = "Dr. Mbarga")
    private String verrouilleParMedecinNom;

    @Schema(description = "Date et heure du verrouillage")
    private LocalDateTime verrouilleAt;
}
