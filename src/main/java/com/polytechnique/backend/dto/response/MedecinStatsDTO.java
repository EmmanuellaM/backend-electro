package com.polytechnique.backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MedecinStatsDTO {
    private int nombreDiagnostics;
    private LocalDateTime derniereConnexion;
    private int diagnosticsAujourdHui;
    private int diagnosticsCetteSemaine;
}
