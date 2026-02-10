package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPerformanceDTO {
    private Integer id;
    private String nom;
    private String email;
    private Long totalDiagnostics;
}
