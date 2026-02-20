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

        // Vérifier si un feedback existe déjà pour ce médecin et ces paramètres
        FeedbackIA feedback;
        if (requestDTO.getParametresId() != null) {
            feedback = feedbackIARepository
                    .findByMedecinIdAndParametresId(requestDTO.getMedecinId(), requestDTO.getParametresId())
                    .orElse(new FeedbackIA());
        } else {
            feedback = new FeedbackIA();
        }

        // Paramètres
        feedback.setAgePatient(requestDTO.getAgePatient());
        feedback.setPoidsPatient(requestDTO.getPoidsPatient());
        feedback.setTaillePatient(requestDTO.getTaillePatient());
        feedback.setTemperature(requestDTO.getTemperature());
        feedback.setPressionSystolique(requestDTO.getPressionSystolique());
        feedback.setPressionDiastolique(requestDTO.getPressionDiastolique());
        feedback.setFrequenceFoetale(requestDTO.getFrequenceFoetale());
        feedback.setFrequenceCardiaqueMere(requestDTO.getFrequenceCardiaqueMere());
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
        if (requestDTO.getParametresId() != null && feedback.getParametres() == null) {
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

    @Override
    @Transactional(readOnly = true)
    public String generateCsv() {
        List<FeedbackIA> feedbacks = feedbackIARepository.findAll();
        StringBuilder csv = new StringBuilder();

        // Header
        csv.append(
                "id;date;medecin;age;poids;taille;temp;sys;dia;fcf;fcm;glycemie;classe_predite;score_confiance;note_medecin;commentaire\n");

        for (FeedbackIA f : feedbacks) {
            csv.append(f.getId()).append(";");
            csv.append(f.getCreatedAt()).append(";");
            csv.append(f.getMedecin() != null ? f.getMedecin().getPrenom() + " " + f.getMedecin().getNom() : "N/A")
                    .append(";");
            csv.append(f.getAgePatient()).append(";");
            csv.append(f.getPoidsPatient()).append(";");
            csv.append(f.getTaillePatient()).append(";");
            csv.append(f.getTemperature()).append(";");
            csv.append(f.getPressionSystolique()).append(";");
            csv.append(f.getPressionDiastolique()).append(";");
            csv.append(f.getFrequenceFoetale()).append(";");
            csv.append(f.getFrequenceCardiaqueMere()).append(";");
            csv.append(f.getGlycemie() != null ? f.getGlycemie() : "").append(";");
            csv.append(f.getClassePredite()).append(";");
            csv.append(f.getScoreConfiance()).append(";");
            csv.append(f.getNoteMedecin()).append(";");
            csv.append(
                    f.getCommentaireMedecin() != null ? f.getCommentaireMedecin().replace("\n", " ").replace(";", ",")
                            : "");
            csv.append("\n");
        }

        return csv.toString();
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
        dto.setFrequenceCardiaqueMere(feedback.getFrequenceCardiaqueMere());
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
