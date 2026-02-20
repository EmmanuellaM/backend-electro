package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.response.StatistiquesResponseDTO;

public interface StatistiquesService {
    StatistiquesResponseDTO getStatistiques(Integer adminId);

    com.polytechnique.backend.dto.response.TrendsResponseDTO getTrends(Integer adminId);
}
