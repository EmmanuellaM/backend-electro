package com.polytechnique.backend.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String email;
    private String nom;
    private String prenom;
    private List<String> roles;

    public JwtResponse(String accessToken, Integer id, String email, String nom, String prenom, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.roles = roles;
    }
}
