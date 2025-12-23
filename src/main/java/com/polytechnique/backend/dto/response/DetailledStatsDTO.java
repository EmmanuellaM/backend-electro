package com.polytechnique.backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class DetailledStatsDTO {
    private Map<String, Long> diagnosticsParMedecin;
    private Map<String, Long> diagnosticsParCentre;
    private long nombreTotalPatients;
    private long nombreTotalMedecins;
    private long nombreTotalDispositifs;
    private double temperatureMoyenne;
}
