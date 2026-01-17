package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour renvoyer les informations des paramètres médicaux
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametresResponseDTO {

    private Integer id;
    private String identifiantPatient;
    private Integer agePatient;
    private BigDecimal poidsPatient;
    private BigDecimal temperature;
    private Integer pressionArterielleSystolique;
    private Integer pressionArterielleDiastolique;
    private Integer frequenceFoetale;
    private BigDecimal glycemie;
    private Integer semaineGrossesse;
    private LocalDateTime dateMesure;
    private String statut; // en_attente, diagnostique, archive

    // Informations du dispositif
    private Integer dispositifId;
    private String nomCentreDeSante;
    private String codeDispositif;

    // Optionnel: nombre de diagnostics
    private int nombreDiagnostics;

    // Informations de verrouillage
    private Integer verrouilleParMedecinId;
    private LocalDateTime verrouilleAt;

    /**
     * Méthode utilitaire pour obtenir la tension sous forme "120/80"
     */
    public String getTensionFormatee() {
        if (pressionArterielleSystolique != null && pressionArterielleDiastolique != null) {
            return pressionArterielleSystolique + "/" + pressionArterielleDiastolique;
        }
        return null;
    }
}