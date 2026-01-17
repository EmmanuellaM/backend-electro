package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour renvoyer les informations d'un médecin
 * Utilisé pour envoyer les données au client
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedecinResponseDTO {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String numeroCarteIdentite;

    // Status
    private String statut;

    private LocalDateTime dateInscription;
    private LocalDateTime derniereConnexion;

    // Optionnel: nombre de diagnostics établis
    private int nombreDiagnostics;
}