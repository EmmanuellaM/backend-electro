package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.polytechnique.backend.status.StatutMedecin;
import com.polytechnique.backend.entity.SpecialiteMedecin;
import com.polytechnique.backend.entity.Genre;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO pour renvoyer les informations d'un médecin
 * Utilisé pour envoyer les données au client
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informations d'un médecin")
public class MedecinResponseDTO {

    @Schema(description = "Identifiant unique du médecin", example = "1")
    private int id;

    @Schema(description = "Nom de famille", example = "Kamga")
    private String nom;

    @Schema(description = "Prénom", example = "Paul")
    private String prenom;

    @Schema(description = "Adresse email", example = "dr.kamga@hopital.cm")
    private String email;

    @Schema(description = "Numéro de téléphone", example = "+237677123456")
    private String tel;

    @Schema(description = "Numéro de carte nationale d'identité", example = "CM-123456789")
    private String numeroCarteIdentite;

    @Schema(description = "Genre du médecin", example = "MASCULIN")
    private Genre genre;

    @Schema(description = "Spécialité médicale", example = "GYNECOLOGIE_OBSTETRIQUE")
    private SpecialiteMedecin specialite;

    @Schema(description = "Statut du médecin", example = "ACTIF")
    private StatutMedecin statut;

    @Schema(description = "Date d'inscription", example = "2026-01-15T10:30:00")
    private LocalDateTime dateInscription;

    @Schema(description = "Date de dernière connexion", example = "2026-01-18T14:45:00")
    private LocalDateTime derniereConnexion;

    @Schema(description = "Nombre de diagnostics établis", example = "5")
    private int nombreDiagnostics;
}