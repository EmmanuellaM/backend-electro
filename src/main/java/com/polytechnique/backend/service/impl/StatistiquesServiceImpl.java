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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        public StatistiquesResponseDTO getStatistiques() {
                // Dispositifs
                List<Dispositif> allDispositifs = dispositifRepository.findAll();
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
                List<Medecin> allMedecins = medecinRepository.findAll();
                int totalMedecins = allMedecins.size();
                int medecinsActifs = (int) allMedecins.stream()
                                .filter(m -> StatutMedecin.ACTIF.equals(m.getStatut()))
                                .count();
                int medecinsSuspendus = (int) allMedecins.stream()
                                .filter(m -> StatutMedecin.SUSPENDU.equals(m.getStatut()))
                                .count();
                int medecinsInactifs = totalMedecins - medecinsActifs - medecinsSuspendus;

                // Infirmiers
                List<InfirmierLocal> allInfirmiers = infirmierLocalRepository.findAll();
                int totalInfirmiers = allInfirmiers.size();
                int infirmiersActifs = (int) allInfirmiers.stream()
                                .filter(i -> "actif".equalsIgnoreCase(i.getStatut()))
                                .count();

                // Patients (distinct identifiantPatient from Parametres)
                long totalPatients = parametresRepository.countDistinctPatients();

                // Patients par statut
                List<com.polytechnique.backend.entity.Parametres> allParametres = parametresRepository.findAll();
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

                // Paramètres en attente (nombre total de mesures sans diagnostic)
                int totalParametres = allParametres.size();
                int parametresEnAttente = (int) allParametres.stream()
                                .filter(p -> "en_attente".equalsIgnoreCase(p.getStatut()))
                                .count();

                // Diagnostics
                long totalDiagnostics = diagnosticRepository.count();

                // Diagnostics par période - basé sur createdAt réel
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                java.time.LocalDateTime yesterday = now.minusHours(24);
                java.time.LocalDateTime weekStart = now.minusDays(7);
                java.time.LocalDateTime monthStart = now.minusDays(30);

                int diagnosticsAujourdHui = (int) diagnosticRepository.countByCreatedAtAfter(yesterday);
                int diagnosticsCetteSemaine = (int) diagnosticRepository.countByCreatedAtAfter(weekStart);
                int diagnosticsCeMois = (int) diagnosticRepository.countByCreatedAtAfter(monthStart);

                // Métriques calculées
                double tauxReponse = totalPatients > 0
                                ? (double) patientsDiagnostiques / totalPatients * 100
                                : 0.0;
                tauxReponse = Math.round(tauxReponse * 10) / 10.0; // Arrondi à 1 décimale

                String tempsReponseMoyen = "2h 15min"; // Valeur par défaut, nécessite un champ timestamp

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
