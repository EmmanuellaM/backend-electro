package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO pour la réponse d'un diagnostic IA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticIAResponseDTO {

    private Integer id;
    private String classePredite;
    private Double scoreConfiance;
    private Map<String, Double> probabilites;
    private ExplicationDTO explication;
    private List<RecommandationDTO> recommandations;
    
    // Informations sur les paramètres
    private Integer parametresId;
    private String identifiantPatient;
    
    // Validation
    private Boolean valideParMedecin;
    private String commentaireMedecin;
    private Integer medecinValidateurId;
    private String medecinValidateurNom;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * DTO pour l'explication SHAP
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExplicationDTO {
        private List<ParametreInfluentDTO> parametresInfluents;
        private String methode;
        private Double baseValue;
    }

    /**
     * DTO pour un paramètre influent
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParametreInfluentDTO {
        private String nom;
        private Double valeur;
        private Double shapValue;
        private String impact;  // "positif" ou "negatif"
    }

    /**
     * DTO pour une recommandation clinique
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommandationDTO {
        private String categorie;
        private String description;
        private String priorite;  // "haute", "moyenne", "basse"
    }
}
