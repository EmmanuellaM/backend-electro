package com.polytechnique.backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DispositifStatsDTO {
    private long nombreParametres;
    private LocalDateTime derniereActivite;
    private String statutActuel;
    private String emplacement;
}
