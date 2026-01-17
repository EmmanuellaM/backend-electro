package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictResponseDTO {

    private String classePredite;
    private Double scoreConfiance;
    private Map<String, Double> probabilites;

    // Le message formatté pour le médecin
    private String explicationMedecin;

    // Données brutes pour affichage avancé si nécessaire
    private Map<String, Object> explicationSHAP;
    private List<Map<String, Object>> recommandationsIA;
}
