package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.DiagnosticRequestDTO;
import com.polytechnique.backend.dto.response.DiagnosticResponseDTO;
import com.polytechnique.backend.entity.Diagnostic;
import com.polytechnique.backend.entity.Medecin;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.mapper.DiagnosticMapper;
import com.polytechnique.backend.repository.DiagnosticRepository;
import com.polytechnique.backend.repository.MedecinRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import com.polytechnique.backend.service.DiagnosticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service pour gérer les diagnostics
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DiagnosticServiceImpl implements DiagnosticService {

    private final DiagnosticRepository diagnosticRepository;
    private final MedecinRepository medecinRepository;
    private final ParametresRepository parametresRepository;
    private final DiagnosticMapper diagnosticMapper;

    @Override
    public DiagnosticResponseDTO createDiagnostic(DiagnosticRequestDTO requestDTO) {
        // Récupérer le médecin
        Medecin medecin = medecinRepository.findById(requestDTO.getMedecinId())
                .orElseThrow(() -> new ResourceNotFoundException("Médecin", "id", requestDTO.getMedecinId()));

        // Récupérer les paramètres
        Parametres parametres = parametresRepository.findById(requestDTO.getParametresId())
                .orElseThrow(() -> new ResourceNotFoundException("Paramètres", "id", requestDTO.getParametresId()));

        // Convertir DTO → Entité
        Diagnostic diagnostic = diagnosticMapper.toEntity(requestDTO);
        diagnostic.setMedecin(medecin);
        diagnostic.setParametres(parametres);

        // Sauvegarder
        Diagnostic savedDiagnostic = diagnosticRepository.save(diagnostic);

        // Convertir Entité → DTO de réponse
        return diagnosticMapper.toResponseDTO(savedDiagnostic);
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticResponseDTO getDiagnosticById(int id) {
        Diagnostic diagnostic = diagnosticRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnostic", "id", id));

        return diagnosticMapper.toResponseDTO(diagnostic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticResponseDTO> getAllDiagnostics() {
        return diagnosticRepository.findAll()
                .stream()
                .map(diagnosticMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DiagnosticResponseDTO updateDiagnostic(int id, DiagnosticRequestDTO requestDTO) {
        // Récupérer le diagnostic existant
        Diagnostic diagnostic = diagnosticRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnostic", "id", id));

        // Si le médecin a changé, récupérer le nouveau
        if (requestDTO.getMedecinId() != null &&
                requestDTO.getMedecinId() != diagnostic.getMedecin().getId()) {
            Medecin newMedecin = medecinRepository.findById(requestDTO.getMedecinId())
                    .orElseThrow(() -> new ResourceNotFoundException("Médecin", "id", requestDTO.getMedecinId()));
            diagnostic.setMedecin(newMedecin);
        }

        // Si les paramètres ont changé, récupérer les nouveaux
        if (requestDTO.getParametresId() != null &&
                requestDTO.getParametresId() != diagnostic.getParametres().getId()) {
            Parametres newParametres = parametresRepository.findById(requestDTO.getParametresId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paramètres", "id", requestDTO.getParametresId()));
            diagnostic.setParametres(newParametres);
        }

        // Mettre à jour l'entité
        diagnosticMapper.updateEntity(requestDTO, diagnostic);

        // Sauvegarder
        Diagnostic updatedDiagnostic = diagnosticRepository.save(diagnostic);

        return diagnosticMapper.toResponseDTO(updatedDiagnostic);
    }

    @Override
    public void deleteDiagnostic(int id) {
        // Vérifier que le diagnostic existe
        if (!diagnosticRepository.existsById(id)) {
            throw new ResourceNotFoundException("Diagnostic", "id", id);
        }

        diagnosticRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticResponseDTO> getDiagnosticsByMedecin(int medecinId) {
        // Vérifier que le médecin existe
        if (!medecinRepository.existsById(medecinId)) {
            throw new ResourceNotFoundException("Médecin", "id", medecinId);
        }

        return diagnosticRepository.findByMedecinId(medecinId)
                .stream()
                .map(diagnosticMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticResponseDTO> getDiagnosticsByParametres(int parametresId) {
        // Vérifier que les paramètres existent
        if (!parametresRepository.existsById(parametresId)) {
            throw new ResourceNotFoundException("Paramètres", "id", parametresId);
        }

        return diagnosticRepository.findByParametresId(parametresId)
                .stream()
                .map(diagnosticMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}