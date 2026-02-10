package com.polytechnique.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Statistiques globales de la plateforme")
public class StatistiquesResponseDTO {

    @Schema(description = "Nombre total de dispositifs", example = "6")
    private int totalDispositifs;

    @Schema(description = "Nombre de dispositifs actifs", example = "4")
    private int dispositifsActifs;

    @Schema(description = "Nombre de dispositifs inactifs", example = "1")
    private int dispositifsInactifs;

    @Schema(description = "Nombre de dispositifs en maintenance", example = "1")
    private int dispositifsMaintenance;

    @Schema(description = "Nombre de dispositifs en attente d'activation", example = "1")
    private int dispositifsEnAttente;

    @Schema(description = "Nombre de dispositifs non attribués", example = "2")
    private int dispositifsNonAttribue;

    @Schema(description = "Nombre total de médecins", example = "5")
    private int totalMedecins;

    @Schema(description = "Nombre de médecins actifs", example = "4")
    private int medecinsActifs;

    @Schema(description = "Nombre de médecins inactifs", example = "1")
    private int medecinsInactifs;

    @Schema(description = "Nombre de médecins suspendus", example = "0")
    private int medecinsSuspendus;

    @Schema(description = "Nombre total de patients (identifiants uniques)", example = "45")
    private int totalPatients;

    @Schema(description = "Nombre de patients en attente de diagnostic", example = "12")
    private int patientsEnAttente;

    @Schema(description = "Nombre de patients diagnostiqués", example = "33")
    private int patientsDiagnostiques;

    @Schema(description = "Nombre total de paramètres médicaux", example = "120")
    private int totalParametres;

    @Schema(description = "Nombre de paramètres en attente de diagnostic", example = "15")
    private int parametresEnAttente;

    @Schema(description = "Nombre total de diagnostics effectués", example = "565")
    private int totalDiagnostics;

    @Schema(description = "Nombre de diagnostics aujourd'hui", example = "12")
    private int diagnosticsAujourdHui;

    @Schema(description = "Nombre de diagnostics cette semaine", example = "42")
    private int diagnosticsCetteSemaine;

    @Schema(description = "Nombre de diagnostics ce mois", example = "180")
    private int diagnosticsCeMois;

    @Schema(description = "Taux de réponse en pourcentage", example = "94.5")
    private double tauxReponse;

    @Schema(description = "Nombre total d'infirmiers", example = "8")
    private int totalInfirmiers;

    @Schema(description = "Nombre d'infirmiers actifs", example = "7")
    private int infirmiersActifs;
}
