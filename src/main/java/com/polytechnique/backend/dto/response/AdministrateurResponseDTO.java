package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministrateurResponseDTO {
    private Integer id;
    private String nom;
    private String email;
    private String role;
    private String statut;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
