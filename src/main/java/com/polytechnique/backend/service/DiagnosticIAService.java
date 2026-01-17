package com.polytechnique.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytechnique.backend.dto.request.DiagnosticIARequestDTO;
import com.polytechnique.backend.dto.request.DiagnosticIAValidationDTO;
import com.polytechnique.backend.dto.response.DiagnosticIAResponseDTO;
import com.polytechnique.backend.entity.DiagnosticIA;
import com.polytechnique.backend.entity.Medecin;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.repository.DiagnosticIARepository;
import com.polytechnique.backend.repository.MedecinRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service métier pour les diagnostics IA
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DiagnosticIAService {

        private final DiagnosticIARepository diagnosticIARepository;
        private final ParametresRepository parametresRepository;
        private final MedecinRepository medecinRepository;
        private final AIClientService aiClientService;
        private final ObjectMapper objectMapper;

        /**
         * Crée un diagnostic IA basé sur des paramètres médicaux
         */
        @Transactional
        public DiagnosticIAResponseDTO createDiagnosticIA(DiagnosticIARequestDTO requestDTO) {
                log.info("Création d'un diagnostic IA pour parametresId: {}", requestDTO.getParametresId());

                // Récupérer les paramètres
                Parametres parametres = parametresRepository.findById(requestDTO.getParametresId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Parametres", "id", requestDTO.getParametresId().toString()));

                // Vérifier si un diagnostic IA existe déjà pour ces paramètres
                diagnosticIARepository.findByParametresId(requestDTO.getParametresId())
                                .ifPresent(existing -> {
                                        throw new IllegalStateException(
                                                        "Un diagnostic IA existe déjà pour ces paramètres (ID: "
                                                                        + existing.getId() + ")");
                                });

                // Appeler le service IA
                Map<String, Object> aiResponse = aiClientService.predictDiagnosis(
                                parametres.getAgePatient(),
                                parametres.getPoidsPatient().doubleValue(),
                                parametres.getTemperature().doubleValue(),
                                parametres.getPressionArterielleSystolique(),
                                parametres.getPressionArterielleDiastolique(),
                                parametres.getFrequenceFoetale(),
                                parametres.getGlycemie() != null ? parametres.getGlycemie().doubleValue() : 5.0, // Valeur
                                                                                                                 // par
                                                                                                                 // défaut
                                                                                                                 // si
                                                                                                                 // null
                                requestDTO.getIncludeExplanation());

                // Créer l'entité DiagnosticIA
                DiagnosticIA diagnosticIA = new DiagnosticIA();
                diagnosticIA.setParametres(parametres);
                diagnosticIA.setClassePredite((String) aiResponse.get("classe_predite"));
                diagnosticIA.setScoreConfiance((Double) aiResponse.get("score_confiance"));

                // Sérialiser les données JSON
                try {
                        diagnosticIA.setProbabilites(objectMapper.writeValueAsString(aiResponse.get("probabilites")));
                        diagnosticIA.setRecommandations(
                                        objectMapper.writeValueAsString(aiResponse.get("recommandations")));

                        if (aiResponse.containsKey("explication") && aiResponse.get("explication") != null) {
                                diagnosticIA.setExplicationJson(
                                                objectMapper.writeValueAsString(aiResponse.get("explication")));
                        }
                } catch (JsonProcessingException e) {
                        log.error("Erreur lors de la sérialisation JSON", e);
                        throw new RuntimeException("Erreur lors de la sauvegarde du diagnostic IA", e);
                }

                // Sauvegarder
                DiagnosticIA saved = diagnosticIARepository.save(diagnosticIA);
                log.info("Diagnostic IA créé avec succès: ID={}, Classe={}", saved.getId(), saved.getClassePredite());

                return toResponseDTO(saved);
        }

        /**
         * Récupère un diagnostic IA par son ID
         */
        public DiagnosticIAResponseDTO getDiagnosticIAById(Integer id) {
                DiagnosticIA diagnosticIA = diagnosticIARepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("DiagnosticIA", "id", id.toString()));
                return toResponseDTO(diagnosticIA);
        }

        /**
         * Récupère tous les diagnostics IA
         */
        public List<DiagnosticIAResponseDTO> getAllDiagnosticsIA() {
                return diagnosticIARepository.findAll().stream()
                                .map(this::toResponseDTO)
                                .collect(Collectors.toList());
        }

        /**
         * Récupère le diagnostic IA pour des paramètres donnés
         */
        public DiagnosticIAResponseDTO getDiagnosticIAByParametres(Integer parametresId) {
                DiagnosticIA diagnosticIA = diagnosticIARepository.findByParametresId(parametresId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "DiagnosticIA", "parametresId", parametresId.toString()));
                return toResponseDTO(diagnosticIA);
        }

        /**
         * Récupère l'historique des diagnostics IA d'un patient
         */
        public List<DiagnosticIAResponseDTO> getPatientHistory(String identifiantPatient) {
                List<DiagnosticIA> diagnostics = diagnosticIARepository
                                .findByParametres_IdentifiantPatientOrderByCreatedAtDesc(identifiantPatient);
                return diagnostics.stream()
                                .map(this::toResponseDTO)
                                .collect(Collectors.toList());
        }

        /**
         * Récupère tous les diagnostics IA en attente de validation
         */
        public List<DiagnosticIAResponseDTO> getPendingDiagnostics() {
                List<DiagnosticIA> diagnostics = diagnosticIARepository
                                .findByValideParMedecinIsNullOrderByCreatedAtDesc();
                return diagnostics.stream()
                                .map(this::toResponseDTO)
                                .collect(Collectors.toList());
        }

        /**
         * Valide ou rejette un diagnostic IA
         */
        @Transactional
        public DiagnosticIAResponseDTO validateDiagnostic(Integer diagnosticIAId,
                        DiagnosticIAValidationDTO validationDTO) {
                log.info("Validation du diagnostic IA {} par le médecin {}", diagnosticIAId,
                                validationDTO.getMedecinId());

                DiagnosticIA diagnosticIA = diagnosticIARepository.findById(diagnosticIAId)
                                .orElseThrow(() -> new ResourceNotFoundException("DiagnosticIA", "id",
                                                diagnosticIAId.toString()));

                Medecin medecin = medecinRepository.findById(validationDTO.getMedecinId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Medecin", "id", validationDTO.getMedecinId().toString()));

                diagnosticIA.setValideParMedecin(validationDTO.getIsValid());
                diagnosticIA.setMedecinValidateur(medecin);
                diagnosticIA.setCommentaireMedecin(validationDTO.getCommentaire());

                DiagnosticIA saved = diagnosticIARepository.save(diagnosticIA);
                log.info("Diagnostic IA {} {} par le médecin {}",
                                diagnosticIAId,
                                validationDTO.getIsValid() ? "validé" : "rejeté",
                                medecin.getNom());

                return toResponseDTO(saved);
        }

        /**
         * Convertit une entité en DTO
         */
        private DiagnosticIAResponseDTO toResponseDTO(DiagnosticIA entity) {
                DiagnosticIAResponseDTO dto = new DiagnosticIAResponseDTO();
                dto.setId(entity.getId());
                dto.setClassePredite(entity.getClassePredite());
                dto.setScoreConfiance(entity.getScoreConfiance());

                // Paramètres
                dto.setParametresId(entity.getParametres().getId());
                dto.setIdentifiantPatient(entity.getParametres().getIdentifiantPatient());

                // Validation
                dto.setValideParMedecin(entity.getValideParMedecin());
                dto.setCommentaireMedecin(entity.getCommentaireMedecin());

                if (entity.getMedecinValidateur() != null) {
                        dto.setMedecinValidateurId(entity.getMedecinValidateur().getId());
                        dto.setMedecinValidateurNom(
                                        entity.getMedecinValidateur().getNom() + " "
                                                        + entity.getMedecinValidateur().getPrenom());
                }

                dto.setCreatedAt(entity.getCreatedAt());
                dto.setUpdatedAt(entity.getUpdatedAt());

                // Désérialiser les JSON
                try {
                        if (entity.getProbabilites() != null) {
                                dto.setProbabilites(objectMapper.readValue(entity.getProbabilites(), Map.class));
                        }

                        if (entity.getRecommandations() != null) {
                                List<Map<String, Object>> recommandationsRaw = objectMapper.readValue(
                                                entity.getRecommandations(), List.class);
                                List<DiagnosticIAResponseDTO.RecommandationDTO> recommandations = recommandationsRaw
                                                .stream()
                                                .map(r -> new DiagnosticIAResponseDTO.RecommandationDTO(
                                                                (String) r.get("categorie"),
                                                                (String) r.get("description"),
                                                                (String) r.get("priorite")))
                                                .collect(Collectors.toList());
                                dto.setRecommandations(recommandations);
                        }

                        if (entity.getExplicationJson() != null) {
                                Map<String, Object> explicationRaw = objectMapper.readValue(
                                                entity.getExplicationJson(), Map.class);

                                List<Map<String, Object>> parametresInfluentsRaw = (List<Map<String, Object>>) explicationRaw
                                                .get("parametres_influents");

                                List<DiagnosticIAResponseDTO.ParametreInfluentDTO> parametresInfluents = parametresInfluentsRaw
                                                .stream()
                                                .map(p -> new DiagnosticIAResponseDTO.ParametreInfluentDTO(
                                                                (String) p.get("nom"),
                                                                ((Number) p.get("valeur")).doubleValue(),
                                                                ((Number) p.get("shap_value")).doubleValue(),
                                                                (String) p.get("impact")))
                                                .collect(Collectors.toList());

                                DiagnosticIAResponseDTO.ExplicationDTO explication = new DiagnosticIAResponseDTO.ExplicationDTO(
                                                parametresInfluents,
                                                (String) explicationRaw.get("methode"),
                                                ((Number) explicationRaw.get("base_value")).doubleValue());

                                dto.setExplication(explication);
                        }

                } catch (JsonProcessingException e) {
                        log.error("Erreur lors de la désérialisation JSON", e);
                }

                return dto;
        }
}
