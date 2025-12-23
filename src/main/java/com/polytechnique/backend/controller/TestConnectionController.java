package com.polytechnique.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test Connection", description = "Endpoints de test de connectivité")
public class TestConnectionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Operation(summary = "Tester la connexion BDD", description = "Vérifie que l'application peut communiquer avec la base de données.")
    @ApiResponse(responseCode = "200", description = "Connexion réussie")
    @GetMapping("/connection")
    public Map<String, Object> testConnection() {
        try {
            // Test simple de connexion
            String version = jdbcTemplate.queryForObject(
                    "SELECT version()",
                    String.class);

            // Compter les médecins
            Integer nbMedecins = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM Medecin",
                    Integer.class);

            return Map.of(
                    "status", "success",
                    "message", "Connexion réussie !",
                    "database_version", version,
                    "nb_medecins", nbMedecins);
        } catch (Exception e) {
            return Map.of(
                    "status", "error",
                    "message", e.getMessage());
        }
    }
}