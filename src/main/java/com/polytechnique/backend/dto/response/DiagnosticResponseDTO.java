package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour renvoyer les informations d'un diagnostic
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticResponseDTO {

    private Integer id;
    private String contenu;
    private String recommandations;
    private String niveauUrgence;

    private LocalDateTime dateDiagnostic;
    private LocalDateTime dateValidation;

    // Informations du médecin
    private Integer medecinId;
    private String medecinNom;
    private String medecinPrenom;
    private String medecinEmail;

    // Informations des paramètres du patient
    private Integer parametresId;
    private String identifiantPatient;
    private BigDecimal poidsPatient;
    private BigDecimal temperature;
    private Integer pressionArterielleSystolique;
    private Integer pressionArterielleDiastolique;
    private Integer frequenceFoetale;
    private Integer frequenceCardiaqueMere;
    private LocalDateTime dateMesure;
    private BigDecimal glycemie;

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