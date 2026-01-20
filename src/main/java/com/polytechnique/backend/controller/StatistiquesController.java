package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.response.StatistiquesResponseDTO;
import com.polytechnique.backend.service.StatistiquesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistiques")
@RequiredArgsConstructor
@Tag(name = "Statistiques", description = "Statistiques globales de la plateforme")
public class StatistiquesController {

    private final StatistiquesService statistiquesService;

    @GetMapping
    @Operation(summary = "Obtenir les statistiques globales", description = "Retourne toutes les statistiques agrégées pour le dashboard admin")
    public ResponseEntity<StatistiquesResponseDTO> getStatistiques(
            @RequestParam(required = false) Integer adminId) {
        return ResponseEntity.ok(statistiquesService.getStatistiques(adminId));
    }
}
