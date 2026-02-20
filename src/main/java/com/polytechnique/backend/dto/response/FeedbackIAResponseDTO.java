package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour un feedback IA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackIAResponseDTO {

    private Integer id;

    // Paramètres
    private Integer agePatient;
    private Double poidsPatient;
    private Double taillePatient;
    private Double temperature;
    private Integer pressionSystolique;
    private Integer pressionDiastolique;
    private Integer frequenceFoetale;
    private Integer frequenceCardiaqueMere;
    private Double glycemie;

    // Résultat IA
    private String classePredite;
    private Double scoreConfiance;

    // Feedback
    private Integer noteMedecin;
    private String commentaireMedecin;

    // Références
    private Integer medecinId;
    private String medecinNom;
    private Integer parametresId;

    private LocalDateTime createdAt;
}
