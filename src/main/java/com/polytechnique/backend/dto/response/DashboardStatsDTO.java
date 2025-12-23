package com.polytechnique.backend.dto.response;

import java.math.BigDecimal;

public class DashboardStatsDTO {
    private long totalPatients;
    private BigDecimal averageWeight;
    private BigDecimal averageTemperature;
    private Double averageFetalFrequency;

    // Getters and Setters
    public long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(long totalPatients) {
        this.totalPatients = totalPatients;
    }

    public BigDecimal getAverageWeight() {
        return averageWeight;
    }

    public void setAverageWeight(BigDecimal averageWeight) {
        this.averageWeight = averageWeight;
    }

    public BigDecimal getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(BigDecimal averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public Double getAverageFetalFrequency() {
        return averageFetalFrequency;
    }

    public void setAverageFetalFrequency(Double averageFetalFrequency) {
        this.averageFetalFrequency = averageFetalFrequency;
    }
}
