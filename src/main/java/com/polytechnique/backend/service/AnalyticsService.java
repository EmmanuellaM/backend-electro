package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.response.DashboardStatsDTO;
import com.polytechnique.backend.dto.response.DetailledStatsDTO;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.entity.Medecin;
import com.polytechnique.backend.repository.DiagnosticRepository;
import com.polytechnique.backend.repository.DispositifRepository;
import com.polytechnique.backend.repository.MedecinRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ParametresRepository parametresRepository;
    private final MedecinRepository medecinRepository;
    private final DispositifRepository dispositifRepository;
    private final DiagnosticRepository diagnosticRepository;

    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO dto = new DashboardStatsDTO();
        dto.setTotalPatients(parametresRepository.countDistinctPatients());
        dto.setAverageWeight(parametresRepository.getAveragePoidsPatient());
        dto.setAverageTemperature(parametresRepository.getAverageTemperature());
        dto.setAverageFetalFrequency(parametresRepository.getAverageFrequenceFoetale());
        return dto;
    }

    @Transactional(readOnly = true)
    public DetailledStatsDTO getDetailledStats() {
        // Prepare maps
        Map<String, Long> diagPerMedecin = new HashMap<>();
        List<Object[]> diagPerMedecinRaw = diagnosticRepository.countDiagnosticsPerMedecin();
        // Assuming raw is [id, count], fetching Medecin name might be better or using
        // the ID.
        // Ideally the repository query should return Name + Count
        // For now using ID as key
        for (Object[] row : diagPerMedecinRaw) {
            diagPerMedecin.put(String.valueOf(row[0]), (Long) row[1]);
        }

        Map<String, Long> diagPerCentre = new HashMap<>();
        List<Object[]> diagPerCentreRaw = diagnosticRepository.countDiagnosticsPerCentre();
        for (Object[] row : diagPerCentreRaw) {
            diagPerCentre.put((String) row[0], (Long) row[1]);
        }

        return DetailledStatsDTO.builder()
                .diagnosticsParMedecin(diagPerMedecin)
                .diagnosticsParCentre(diagPerCentre)
                .nombreTotalPatients(parametresRepository.countDistinctPatients())
                .nombreTotalMedecins(medecinRepository.count())
                .nombreTotalDispositifs(dispositifRepository.count())
                .temperatureMoyenne(parametresRepository.getAverageTemperature() != null
                        ? parametresRepository.getAverageTemperature().doubleValue()
                        : 0.0)
                .build();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> globalSearch(String query) {
        Map<String, Object> results = new HashMap<>();
        results.put("medecins", medecinRepository.searchMedecins(query));
        results.put("dispositifs", dispositifRepository.searchDispositifs(query));
        results.put("diagnostics", diagnosticRepository.searchByKeyword(query));
        return results;
    }

    public List<Parametres> findFeverParameters() {
        return parametresRepository.findParametresAvecFievre();
    }

    public List<Parametres> findHypothermiaParameters() {
        return parametresRepository.findParametresAvecHypothermie();
    }

    public List<Parametres> findAbnormalFetalFrequencyParameters() {
        return parametresRepository.findParametresAvecFrequenceFoetaleAnormale();
    }

    public List<Medecin> searchMedecins(String term) {
        return medecinRepository.searchMedecins(term);
    }
}
