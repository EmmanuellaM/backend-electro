package com.polytechnique.backend.dto.response;

import lombok.Data;

@Data
public class InfirmierLocalResponseDTO {
    private Integer id;
    private String nom;
    private String prenom;
    private String telephone1;
    private String telephone2;
    private String zoneAffectation;
    private String statut;
}
