package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.polytechnique.backend.status.StatutDispositif;

/**
 * DTO pour renvoyer les informations d'un dispositif
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispositifResponseDTO {

    private Integer id;
    private String deveui;
    private String appeui;
    private String appkey;
    private String nomCentreDeSante;
    private String localisation;
    private String contact;
    private StatutDispositif statut;
    private LocalDate dateInstallation;
    private LocalDateTime createdAt;

    // Informations complètes ou partielles de l'infirmier
    private InfirmierLocalResponseDTO infirmierLocal;

    // Informations sur l'administrateur
    private String administrateurNom;
    private Integer administrateurId;

    // Optionnel
    private int nombreParametres;
}