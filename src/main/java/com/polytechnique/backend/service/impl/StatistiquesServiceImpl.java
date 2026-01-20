package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.response.StatistiquesResponseDTO;
import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.entity.InfirmierLocal;
import com.polytechnique.backend.entity.Medecin;
import com.polytechnique.backend.repository.*;
import com.polytechnique.backend.service.StatistiquesService;
import com.polytechnique.backend.entity.StatutDispositif;
import com.polytechnique.backend.entity.StatutMedecin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatistiquesServiceImpl implements StatistiquesService {

        private final DispositifRepository dispositifRepository;
        private final MedecinRepository medecinRepository;
        private final ParametresRepository parametresRepository;
        private final DiagnosticRepository diagnosticRepository;
        private final InfirmierLocalRepository infirmierLocalRepository;

        @Override
        public StatistiquesResponseDTO getStatistiques(Integer adminId) {
                // Dispositifs
                List<Dispositif> allDispositifs;
                if (adminId != null) {
                        allDispositifs = dispositifRepository.findByAdministrateurId(adminId);
                } else {
                        allDispositifs = dispositifRepository.findAll();
                }

                int totalDispositifs = allDispositifs.size();
                int dispositifsActifs = (int) allDispositifs.stream()
                                .filter(d -> StatutDispositif.ACTIF.equals(d.getStatut()))
                                .count();
                int dispositifsInactifs = (int) allDispositifs.stream()
                                .filter(d -> StatutDispositif.INACTIF.equals(d.getStatut()))
                                .count();
                int dispositifsMaintenance = (int) allDispositifs.stream()
                                .filter(d -> StatutDispositif.MAINTENANCE.equals(d.getStatut()))
                                .count();
                int dispositifsEnAttente = (int) allDispositifs.stream()
                                .filter(d -> StatutDispositif.EN_ATTENTE_ACTIVATION.equals(d.getStatut()))
                                .count();

                // Médecins
                List<Medecin> allMedecins;
                if (adminId != null) {
                        allMedecins = medecinRepository.findByAdministrateurId(adminId);
                } else {
                        allMedecins = medecinRepository.findAll();
                }

                int totalMedecins = allMedecins.size();
                int medecinsActifs = (int) allMedecins.stream()
                                .filter(m -> StatutMedecin.ACTIF.equals(m.getStatut()))
                                .count();
                int medecinsSuspendus = (int) allMedecins.stream()
                                .filter(m -> StatutMedecin.SUSPENDU.equals(m.getStatut()))
                                .count();
                int medecinsInactifs = totalMedecins - medecinsActifs - medecinsSuspendus;

                // Infirmiers
                List<InfirmierLocal> allInfirmiers;
                if (adminId != null) {
                        allInfirmiers = infirmierLocalRepository.findByAdministrateurId(adminId);
                } else {
                        allInfirmiers = infirmierLocalRepository.findAll();
                }

                int totalInfirmiers = allInfirmiers.size();
                int infirmiersActifs = (int) allInfirmiers.stream()
                                .filter(i -> "actif".equalsIgnoreCase(i.getStatut()))
                                .count();

                // Patients
                long totalPatients;
                if (adminId != null) {
                        totalPatients = parametresRepository.countDistinctPatientsByAdministrateurId(adminId);
                } else {
                        totalPatients = parametresRepository.countDistinctPatients();
                }

                // Patients par statut & Paramètres
                List<com.polytechnique.backend.entity.Parametres> allParametres;
                if (adminId != null) {
                        allParametres = parametresRepository.findAllByAdministrateurId(adminId);
                } else {
                        allParametres = parametresRepository.findAll();
                }

                int patientsEnAttente = (int) allParametres.stream()
                                .filter(p -> "en_attente".equalsIgnoreCase(p.getStatut()))
                                .map(p -> p.getIdentifiantPatient())
                                .distinct()
                                .count();
                int patientsDiagnostiques = (int) allParametres.stream()
                                .filter(p -> "diagnostique".equalsIgnoreCase(p.getStatut()))
                                .map(p -> p.getIdentifiantPatient())
                                .distinct()
                                .count();

                int totalParametres = allParametres.size();
                int parametresEnAttente = (int) allParametres.stream()
                                .filter(p -> "en_attente".equalsIgnoreCase(p.getStatut()))
                                .count();

                // Diagnostics
                long totalDiagnostics;
                List<com.polytechnique.backend.entity.Diagnostic> allDiagnostics = null; // Used for some counts if
                                                                                         // needed, but here we use
                                                                                         // repository for dates

                if (adminId != null) {
                        // For specific counts often repository is better, but here we used count()
                        // globally.
                        // Let's implement logic consistent with "all or filtered".
                        // Since we added findAllByAdministrateurId, we can use that for total count
                        allDiagnostics = diagnosticRepository.findAllByAdministrateurId(adminId);
                        totalDiagnostics = allDiagnostics.size();
                } else {
                        totalDiagnostics = diagnosticRepository.count();
                }

                // Diagnostics par période
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                java.time.LocalDateTime yesterday = now.minusHours(24);
                java.time.LocalDateTime weekStart = now.minusDays(7);
                java.time.LocalDateTime monthStart = now.minusDays(30);

                int diagnosticsAujourdHui;
                int diagnosticsCetteSemaine;
                int diagnosticsCeMois;

                if (adminId != null) {
                        diagnosticsAujourdHui = (int) diagnosticRepository
                                        .countByAdministrateurIdAndCreatedAtAfter(adminId, yesterday);
                        diagnosticsCetteSemaine = (int) diagnosticRepository
                                        .countByAdministrateurIdAndCreatedAtAfter(adminId, weekStart);
                        diagnosticsCeMois = (int) diagnosticRepository.countByAdministrateurIdAndCreatedAtAfter(adminId,
                                        monthStart);
                } else {
                        diagnosticsAujourdHui = (int) diagnosticRepository.countByCreatedAtAfter(yesterday);
                        diagnosticsCetteSemaine = (int) diagnosticRepository.countByCreatedAtAfter(weekStart);
                        diagnosticsCeMois = (int) diagnosticRepository.countByCreatedAtAfter(monthStart);
                }

                // Métriques calculées
                double tauxReponse = totalPatients > 0
                                ? (double) patientsDiagnostiques / totalPatients * 100
                                : 0.0;
                tauxReponse = Math.round(tauxReponse * 10) / 10.0;

                String tempsReponseMoyen = "2h 15min";

                return StatistiquesResponseDTO.builder()
                                .totalDispositifs(totalDispositifs)
                                .dispositifsActifs(dispositifsActifs)
                                .dispositifsInactifs(dispositifsInactifs)
                                .dispositifsMaintenance(dispositifsMaintenance)
                                .totalMedecins(totalMedecins)
                                .medecinsActifs(medecinsActifs)
                                .medecinsInactifs(medecinsInactifs)
                                .medecinsSuspendus(medecinsSuspendus)
                                .dispositifsEnAttente(dispositifsEnAttente)
                                .totalPatients((int) totalPatients)
                                .patientsEnAttente(patientsEnAttente)
                                .patientsDiagnostiques(patientsDiagnostiques)
                                .totalParametres(totalParametres)
                                .parametresEnAttente(parametresEnAttente)
                                .totalDiagnostics((int) totalDiagnostics)
                                .diagnosticsAujourdHui(diagnosticsAujourdHui)
                                .diagnosticsCetteSemaine(diagnosticsCetteSemaine)
                                .diagnosticsCeMois(diagnosticsCeMois)
                                .tauxReponse(tauxReponse)
                                .tempsReponseMoyen(tempsReponseMoyen)
                                .totalInfirmiers(totalInfirmiers)
                                .infirmiersActifs(infirmiersActifs)
                                .build();
        }
}
