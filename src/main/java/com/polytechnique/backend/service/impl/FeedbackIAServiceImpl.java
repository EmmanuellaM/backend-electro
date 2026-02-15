package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.FeedbackIARequestDTO;
import com.polytechnique.backend.dto.response.FeedbackIAResponseDTO;
import com.polytechnique.backend.entity.FeedbackIA;
import com.polytechnique.backend.entity.Medecin;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.repository.FeedbackIARepository;
import com.polytechnique.backend.repository.MedecinRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import com.polytechnique.backend.service.FeedbackIAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service FeedbackIA
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackIAServiceImpl implements FeedbackIAService {

    private final FeedbackIARepository feedbackIARepository;
    private final MedecinRepository medecinRepository;
    private final ParametresRepository parametresRepository;

    @Override
    public FeedbackIAResponseDTO createFeedback(FeedbackIARequestDTO requestDTO) {
        // Récupérer le médecin
        Medecin medecin = medecinRepository.findById(requestDTO.getMedecinId())
                .orElseThrow(() -> new ResourceNotFoundException("Médecin", "id", requestDTO.getMedecinId()));

        // Créer l'entité feedback
        FeedbackIA feedback = new FeedbackIA();

        // Paramètres
        feedback.setAgePatient(requestDTO.getAgePatient());
        feedback.setPoidsPatient(requestDTO.getPoidsPatient());
        feedback.setTaillePatient(requestDTO.getTaillePatient());
        feedback.setTemperature(requestDTO.getTemperature());
        feedback.setPressionSystolique(requestDTO.getPressionSystolique());
        feedback.setPressionDiastolique(requestDTO.getPressionDiastolique());
        feedback.setFrequenceFoetale(requestDTO.getFrequenceFoetale());
        feedback.setGlycemie(requestDTO.getGlycemie());

        // Résultat IA
        feedback.setClassePredite(requestDTO.getClassePredite());
        feedback.setScoreConfiance(requestDTO.getScoreConfiance());
        feedback.setExplicationMedecin(requestDTO.getExplicationMedecin());

        // Feedback médecin
        feedback.setNoteMedecin(requestDTO.getNoteMedecin());
        feedback.setCommentaireMedecin(requestDTO.getCommentaireMedecin());

        // Relations
        feedback.setMedecin(medecin);

        // Paramètres optionnels
        if (requestDTO.getParametresId() != null) {
            Parametres parametres = parametresRepository.findById(requestDTO.getParametresId())
                    .orElse(null);
            feedback.setParametres(parametres);
        }

        FeedbackIA saved = feedbackIARepository.save(feedback);
        return toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackIAResponseDTO getFeedbackById(int id) {
        FeedbackIA feedback = feedbackIARepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedbackIA", "id", id));
        return toResponseDTO(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackIAResponseDTO> getAllFeedbacks() {
        return feedbackIARepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackIAResponseDTO> getFeedbacksByMedecin(int medecinId) {
        return feedbackIARepository.findByMedecinId(medecinId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFeedback(int id) {
        if (!feedbackIARepository.existsById(id)) {
            throw new ResourceNotFoundException("FeedbackIA", "id", id);
        }
        feedbackIARepository.deleteById(id);
    }

    private FeedbackIAResponseDTO toResponseDTO(FeedbackIA feedback) {
        FeedbackIAResponseDTO dto = new FeedbackIAResponseDTO();
        dto.setId(feedback.getId());

        // Paramètres
        dto.setAgePatient(feedback.getAgePatient());
        dto.setPoidsPatient(feedback.getPoidsPatient());
        dto.setTaillePatient(feedback.getTaillePatient());
        dto.setTemperature(feedback.getTemperature());
        dto.setPressionSystolique(feedback.getPressionSystolique());
        dto.setPressionDiastolique(feedback.getPressionDiastolique());
        dto.setFrequenceFoetale(feedback.getFrequenceFoetale());
        dto.setGlycemie(feedback.getGlycemie());

        // Résultat IA
        dto.setClassePredite(feedback.getClassePredite());
        dto.setScoreConfiance(feedback.getScoreConfiance());

        // Feedback
        dto.setNoteMedecin(feedback.getNoteMedecin());
        dto.setCommentaireMedecin(feedback.getCommentaireMedecin());

        // Références
        if (feedback.getMedecin() != null) {
            dto.setMedecinId(feedback.getMedecin().getId());
            dto.setMedecinNom(feedback.getMedecin().getPrenom() + " " + feedback.getMedecin().getNom());
        }
        if (feedback.getParametres() != null) {
            dto.setParametresId(feedback.getParametres().getId());
        }

        dto.setCreatedAt(feedback.getCreatedAt());

        return dto;
    }
}
