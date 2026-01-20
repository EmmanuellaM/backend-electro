package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.ParametresRequestDTO;
import com.polytechnique.backend.dto.response.ParametresResponseDTO;
import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.mapper.ParametresMapper;
import com.polytechnique.backend.repository.DispositifRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import com.polytechnique.backend.service.ParametresService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service pour gérer les paramètres médicaux
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ParametresServiceImpl implements ParametresService {

    private final ParametresRepository parametresRepository;
    private final DispositifRepository dispositifRepository;
    private final ParametresMapper parametresMapper;

    @Override
    public ParametresResponseDTO createParametres(ParametresRequestDTO requestDTO) {
        // Récupérer le dispositif
        Dispositif dispositif = dispositifRepository.findById(requestDTO.getDispositifId())
                .orElseThrow(() -> new ResourceNotFoundException("Dispositif", "id", requestDTO.getDispositifId()));

        // Convertir DTO → Entité
        Parametres parametres = parametresMapper.toEntity(requestDTO);
        parametres.setDispositif(dispositif);

        // Sauvegarder
        Parametres savedParametres = parametresRepository.save(parametres);

        // Convertir Entité → DTO de réponse
        return parametresMapper.toResponseDTO(savedParametres);
    }

    @Override
    @Transactional(readOnly = true)
    public ParametresResponseDTO getParametresById(int id) {
        Parametres parametres = parametresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paramètres", "id", id));

        return parametresMapper.toResponseDTO(parametres);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParametresResponseDTO> getAllParametres() {
        return parametresRepository.findAll()
                .stream()
                .map(parametresMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParametresResponseDTO updateParametres(int id, ParametresRequestDTO requestDTO) {
        // Récupérer les paramètres existants
        Parametres parametres = parametresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paramètres", "id", id));

        // Si le dispositif a changé, récupérer le nouveau
        if (requestDTO.getDispositifId() != null &&
                requestDTO.getDispositifId() != parametres.getDispositif().getId()) {
            Dispositif newDispositif = dispositifRepository.findById(requestDTO.getDispositifId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dispositif", "id", requestDTO.getDispositifId()));
            parametres.setDispositif(newDispositif);
        }

        // Mettre à jour l'entité
        parametresMapper.updateEntity(requestDTO, parametres);

        // Sauvegarder
        Parametres updatedParametres = parametresRepository.save(parametres);

        return parametresMapper.toResponseDTO(updatedParametres);
    }

    @Override
    public void deleteParametres(int id) {
        // Vérifier que les paramètres existent
        if (!parametresRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paramètres", "id", id);
        }

        parametresRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParametresResponseDTO> getParametresByPatient(String identifiantPatient) {
        return parametresRepository.findByIdentifiantPatient(identifiantPatient)
                .stream()
                .map(parametresMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParametresResponseDTO> getParametresByDispositif(int dispositifId) {
        // Vérifier que le dispositif existe
        if (!dispositifRepository.existsById(dispositifId)) {
            throw new ResourceNotFoundException("Dispositif", "id", dispositifId);
        }

        return parametresRepository.findByDispositifId(dispositifId)
                .stream()
                .map(parametresMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParametresResponseDTO lockParametres(int id, int medecinId) {
        Parametres parametres = parametresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paramètres", "id", id));

        // Vérifier si déjà verrouillé par un autre médecin
        if (parametres.getVerrouilleParMedecinId() != null
                && !parametres.getVerrouilleParMedecinId().equals(medecinId)) {
            // Vérifier le timeout (30 minutes)
            if (parametres.getVerrouilleAt() != null
                    && parametres.getVerrouilleAt().plusMinutes(30).isAfter(java.time.LocalDateTime.now())) {
                throw new IllegalStateException(
                        "Ces paramètres sont actuellement consultés par un autre médecin (ID: "
                                + parametres.getVerrouilleParMedecinId() + ")");
            }
            // Timeout expiré, on peut verrouiller
        }

        // Verrouiller
        parametres.setVerrouilleParMedecinId(medecinId);
        parametres.setVerrouilleAt(java.time.LocalDateTime.now());

        Parametres saved = parametresRepository.save(parametres);
        return parametresMapper.toResponseDTO(saved);
    }

    @Override
    public ParametresResponseDTO unlockParametres(int id) {
        Parametres parametres = parametresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paramètres", "id", id));

        // Déverrouiller
        parametres.setVerrouilleParMedecinId(null);
        parametres.setVerrouilleAt(null);

        Parametres saved = parametresRepository.save(parametres);
        return parametresMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLocked(int id, int medecinId) {
        Parametres parametres = parametresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paramètres", "id", id));

        // Pas verrouillé
        if (parametres.getVerrouilleParMedecinId() == null) {
            return false;
        }

        // Verrouillé par le même médecin = pas bloqué pour lui
        if (parametres.getVerrouilleParMedecinId().equals(medecinId)) {
            return false;
        }

        // Vérifier le timeout (30 minutes)
        if (parametres.getVerrouilleAt() != null
                && parametres.getVerrouilleAt().plusMinutes(30).isBefore(java.time.LocalDateTime.now())) {
            return false; // Timeout expiré
        }

        return true; // Verrouillé par un autre médecin
    }

    @Override
    public List<ParametresResponseDTO> getParametresSansDiagnostic(Integer adminId) {
        List<Parametres> parametresList;
        if (adminId != null) {
            parametresList = parametresRepository.findParametresSansDiagnosticsByAdminId(adminId);
        } else {
            parametresList = parametresRepository.findParametresSansDiagnostics();
        }
        return parametresList.stream()
                .map(parametresMapper::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}