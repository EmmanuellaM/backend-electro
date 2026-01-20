package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.response.PatientResponseDTO;
import com.polytechnique.backend.dto.response.ParametresResponseDTO;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.repository.ParametresRepository;
import com.polytechnique.backend.service.ParametresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Gestion des patients (agrégation par identifiantPatient)")
public class PatientController {

        private final ParametresRepository parametresRepository;
        private final ParametresService parametresService;

        @GetMapping
        @Operation(summary = "Liste tous les patients", description = "Retourne la liste des patients uniques avec leurs derniers paramètres")
        public ResponseEntity<List<PatientResponseDTO>> getAllPatients(
                        @Parameter(description = "Filtrer par statut (en_attente, diagnostique, archive)") @RequestParam(required = false) String statut,
                        @Parameter(description = "ID de l'admin (pour filtrage)", example = "1") @RequestParam(required = false) Integer adminId) {

                List<Parametres> allParametres;

                if ("en_attente".equalsIgnoreCase(statut)) {
                        if (adminId != null) {
                                allParametres = parametresRepository.findParametresSansDiagnosticsByAdminId(adminId);
                        } else {
                                allParametres = parametresRepository.findParametresSansDiagnostics();
                        }
                } else {
                        if (adminId != null) {
                                allParametres = parametresRepository.findAllByAdministrateurId(adminId);
                        } else {
                                allParametres = parametresRepository.findAll();
                        }
                }

                // Grouper par identifiantPatient et prendre le plus ANCIEN pour chaque patient
                // (FIFO)
                // Cela permet de traiter les cas dans l'ordre chronologique
                Map<String, Parametres> oldestByPatient = new LinkedHashMap<>();

                // Trier par id croissant (plus ancien en premier)
                allParametres.stream()
                                .sorted(Comparator.comparingInt(Parametres::getId))
                                .forEach(p -> oldestByPatient.putIfAbsent(p.getIdentifiantPatient(), p));

                List<PatientResponseDTO> patients = oldestByPatient.values().stream()
                                .filter(p -> statut == null || statut.equalsIgnoreCase(p.getStatut()))
                                .map(this::mapToPatientDTO)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(patients);
        }

        @GetMapping("/{identifiantPatient}")
        @Operation(summary = "Obtenir un patient par son identifiant", description = "Retourne les détails d'un patient avec ses derniers paramètres")
        public ResponseEntity<PatientResponseDTO> getPatientByIdentifiant(
                        @PathVariable String identifiantPatient) {

                List<Parametres> patientParametres = parametresRepository
                                .findByIdentifiantPatientOrderByIdDesc(identifiantPatient);

                if (patientParametres.isEmpty()) {
                        return ResponseEntity.notFound().build();
                }

                PatientResponseDTO patient = mapToPatientDTO(patientParametres.get(0));
                return ResponseEntity.ok(patient);
        }

        @GetMapping("/{identifiantPatient}/parametres")
        @Operation(summary = "Historique des paramètres d'un patient", description = "Retourne tous les paramètres médicaux d'un patient, triés par date")
        public ResponseEntity<List<ParametresResponseDTO>> getPatientParametres(
                        @PathVariable String identifiantPatient) {

                List<ParametresResponseDTO> parametres = parametresService
                                .getParametresByPatient(identifiantPatient);

                return ResponseEntity.ok(parametres);
        }

        private PatientResponseDTO mapToPatientDTO(Parametres p) {
                PatientResponseDTO.PatientResponseDTOBuilder builder = PatientResponseDTO.builder()
                                .id(p.getId())
                                .identifiantPatient(p.getIdentifiantPatient())
                                .age(p.getAgePatient())
                                .statut(p.getStatut())
                                .dateDerniereMesure(p.getDateMesure())
                                .poidsPatient(p.getPoidsPatient())
                                .temperature(p.getTemperature())
                                .pressionArterielleSystolique(p.getPressionArterielleSystolique())
                                .pressionArterielleDiastolique(p.getPressionArterielleDiastolique())
                                .frequenceFoetale(p.getFrequenceFoetale())
                                .glycemie(p.getGlycemie());

                // Ajouter les infos du dispositif si disponible
                if (p.getDispositif() != null) {
                        builder.dispositifId(p.getDispositif().getId())
                                        .nomCentreDeSante(p.getDispositif().getNomCentreDeSante())
                                        .localisation(p.getDispositif().getLocalisation());
                }

                return builder.build();
        }
}
