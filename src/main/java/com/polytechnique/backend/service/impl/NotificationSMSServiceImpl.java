package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.NotificationSMSRequestDTO;
import com.polytechnique.backend.dto.response.NotificationSMSResponseDTO;
import com.polytechnique.backend.entity.Diagnostic;
import com.polytechnique.backend.entity.InfirmierLocal;
import com.polytechnique.backend.entity.NotificationSMS;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.mapper.NotificationSMSMapper;
import com.polytechnique.backend.repository.DiagnosticRepository;
import com.polytechnique.backend.repository.InfirmierLocalRepository;
import com.polytechnique.backend.repository.NotificationSMSRepository;
import com.polytechnique.backend.service.NotificationSMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationSMSServiceImpl implements NotificationSMSService {

    private final NotificationSMSRepository notificationRepository;
    private final NotificationSMSMapper notificationMapper;
    private final InfirmierLocalRepository infirmierRepository;
    private final DiagnosticRepository diagnosticRepository;

    @Override
    public NotificationSMSResponseDTO createNotification(NotificationSMSRequestDTO requestDTO) {
        NotificationSMS notification = notificationMapper.toEntity(requestDTO);

        if (requestDTO.getInfirmierId() != null) {
            InfirmierLocal infirmier = infirmierRepository.findById(requestDTO.getInfirmierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Infirmier", "id", requestDTO.getInfirmierId()));
            notification.setInfirmierLocal(infirmier);
        }

        if (requestDTO.getDiagnosticId() != null) {
            Diagnostic diagnostic = diagnosticRepository.findById(requestDTO.getDiagnosticId())
                    .orElseThrow(() -> new ResourceNotFoundException("Diagnostic", "id", requestDTO.getDiagnosticId()));
            notification.setDiagnostic(diagnostic);
        }

        NotificationSMS saved = notificationRepository.save(notification);
        return notificationMapper.toResponseDTO(saved);
    }

    @Override
    public void createAutomaticNotification(Diagnostic diagnostic, InfirmierLocal infirmier) {
        NotificationSMS notification = new NotificationSMS();

        // Formatage du message SMS
        String message = String.format("Alerte Patient %s: %s. Recommandations: %s. Urgence: %s",
                diagnostic.getParametres().getIdentifiantPatient(),
                diagnostic.getContenu(),
                diagnostic.getRecommandations(),
                diagnostic.getNiveauUrgence());

        notification.setContenuMessage(message);
        notification.setNumeroDestinataire(infirmier.getTelephone1());
        notification.setInfirmierLocal(infirmier);
        notification.setDiagnostic(diagnostic);
        notification.setSucces(true); // Simulation de succès

        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationSMSResponseDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationSMSResponseDTO getNotificationById(int id) {
        NotificationSMS notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NotificationSMS", "id", id));
        return notificationMapper.toResponseDTO(notification);
    }
}
