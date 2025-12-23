package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.response.DashboardStatsDTO;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Statistiques", description = "Endpoints pour les statistiques et la recherche globale")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(summary = "Tableau de bord", description = "Fournit des statistiques globales.")
    @ApiResponse(responseCode = "200", description = "Données récupérées")
    @GetMapping("/statistiques/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboard() {
        DashboardStatsDTO stats = analyticsService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Statistiques détaillées", description = "Fournit des statistiques détaillées.")
    @ApiResponse(responseCode = "200", description = "Données récupérées")
    @GetMapping("/statistiques/detaillees")
    public ResponseEntity<com.polytechnique.backend.dto.response.DetailledStatsDTO> getDetailledStats() {
        return ResponseEntity.ok(analyticsService.getDetailledStats());
    }

    @Operation(summary = "Recherche globale", description = "Recherche dans médecins, dispositifs et diagnostics.")
    @GetMapping("/search")
    public ResponseEntity<java.util.Map<String, Object>> globalSearch(@RequestParam String query) {
        return ResponseEntity.ok(analyticsService.globalSearch(query));
    }

    @Operation(summary = "Alertes Fièvre", description = "Retourne les paramètres où température > 37.5°C.")
    @GetMapping("/statistiques/alerts/fever")
    public ResponseEntity<List<Parametres>> getFeverAlerts() {
        return ResponseEntity.ok(analyticsService.findFeverParameters());
    }

    @Operation(summary = "Alertes Hypothermie", description = "Retourne les paramètres où température < 36.0°C.")
    @GetMapping("/statistiques/alerts/hypothermie")
    public ResponseEntity<List<Parametres>> getHypothermiaAlerts() {
        return ResponseEntity.ok(analyticsService.findHypothermiaParameters());
    }

    @Operation(summary = "Alertes fréquence fœtale", description = "Retourne les paramètres avec fréquence fœtale anormale.")
    @GetMapping("/statistiques/alerts/fetal-abnormal")
    public ResponseEntity<List<Parametres>> getFetalAbnormalAlerts() {
        return ResponseEntity.ok(analyticsService.findAbnormalFetalFrequencyParameters());
    }
}
