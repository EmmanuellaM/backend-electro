package com.polytechnique.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendsResponseDTO {
    private Map<String, Long> diagnosticsByDay;
    private Map<String, Long> weeklyTrends;
    private Map<String, Long> monthlyTrends;
    private Map<String, Long> quarterlyTrends;
    private Map<String, Long> yearlyTrends;
    private Map<String, Long> emergencyDistribution;
    private Map<String, Long> specialtyDistribution;
    private Map<String, Long> centreActivity;
    private java.util.List<AdminPerformanceDTO> adminPerformance;
}
