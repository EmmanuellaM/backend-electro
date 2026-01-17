package com.polytechnique.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AIExplanationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Génère un rapport textuel pour le médecin à partir de la réponse brute de
     * l'IA
     */
    public String formatDoctorExplanation(Map<String, Object> aiResponse) {
        try {
            String predictedClass = (String) aiResponse.get("classe_predite");
            Double confidence = (Double) aiResponse.get("score_confiance");

            // Conversion safely des objets imbriqués via Jackson pour éviter les cast
            // errors
            JsonNode explicationNode = objectMapper.valueToTree(aiResponse.get("explication"));
            JsonNode recosNode = objectMapper.valueToTree(aiResponse.get("recommandations"));

            return generateReport(predictedClass, confidence, explicationNode, recosNode);
        } catch (Exception e) {
            log.error("Erreur lors du formatage du rapport médical", e);
            return "Analyse IA disponible mais impossible de générer le rapport textuel détaillé.";
        }
    }

    private String generateReport(String predictedClass, Double confidence, JsonNode explicationNode,
            JsonNode recosNode) {
        StringBuilder report = new StringBuilder();

        // 1. En-tête : Diagnostic et Confiance
        String readableClass = formatClassName(predictedClass);
        report.append("**ASSISTANCE IA : ANALYSE DES RISQUES**\n\n");
        report.append(String.format("**Diagnostic Suggéré :** %s\n", readableClass));
        if (confidence != null) {
            report.append(String.format("**Niveau de Confiance :** %.1f%%\n\n", confidence * 100));
        }

        // 2. Explication SHAP (Facteurs déterminants)
        report.append("**🔍 Facteurs Explicatifs (Pourquoi ce résultat ?)**\n");
        report.append("Le modèle a identifié les signes cliniques suivants comme déterminants :\n");

        if (explicationNode != null && explicationNode.has("parametres_influents")) {
            JsonNode params = explicationNode.get("parametres_influents");
            int count = 1;
            for (JsonNode param : params) {
                // On limite aux 4 facteurs les plus importants
                if (count > 4)
                    break;

                String nomParam = formatParamName(param.get("nom").asText());
                String valeur = param.has("valeur") ? param.get("valeur").asText() : "N/A";
                String impact = param.has("impact") ? param.get("impact").asText() : "";

                // Logique simple pour qualifier l'impact visuellement
                String impactLabel = "Impact significatif";
                if ("positif".equalsIgnoreCase(impact)) {
                    impactLabel = "Contribue au risque";
                } else if ("negatif".equalsIgnoreCase(impact)) {
                    impactLabel = "Réduit le risque";
                }

                report.append(String.format("%d. **%s (%s)** : *%s*\n",
                        count++, nomParam, valeur, impactLabel));
            }
        } else {
            report.append("Aucune explication détaillée disponible.\n");
        }

        // 3. Recommandations
        report.append("\n**💡 Recommandations IA :**\n");
        if (recosNode != null && recosNode.isArray()) {
            for (JsonNode reco : recosNode) {
                if (reco.has("description") && reco.has("priorite")) {
                    String desc = reco.get("description").asText();
                    String priorite = reco.get("priorite").asText();
                    report.append(String.format("* %s (%s)\n", desc, formatPriority(priorite)));
                }
            }
        }

        report.append(
                "\n*Note : Cette analyse est basée sur la comparaison avec les motifs historiques de cas similaires.*");

        return report.toString();
    }

    private String formatClassName(String slug) {
        if (slug == null)
            return "Inconnu";
        return switch (slug) {
            case "souffrance_foetale" -> "Souffrance Fœtale";
            case "pre_eclampsie" -> "Pré-éclampsie";
            case "diabete_gestationnel" -> "Diabète Gestationnel";
            case "travail_premature" -> "Travail Prématuré";
            case "infection" -> "Infection";
            case "normal" -> "Normal";
            default -> slug.replace("_", " ");
        };
    }

    private String formatParamName(String param) {
        if (param == null)
            return "";
        return switch (param) {
            case "frequence_foetale" -> "Fréquence Fœtale";
            case "pression_arterielle_systolique" -> "Pression Artérielle (Sys)";
            case "pression_arterielle_diastolique" -> "Pression Artérielle (Dia)";
            case "age_patient" -> "Âge Patiente";
            case "poids_patient" -> "Poids";
            case "glycemie" -> "Glycémie";
            case "temperature" -> "Température";
            default -> param;
        };
    }

    private String formatPriority(String priority) {
        if (priority == null)
            return "";
        return "haute".equalsIgnoreCase(priority) ? "Urgence Haute" : "Priorité " + priority;
    }
}
