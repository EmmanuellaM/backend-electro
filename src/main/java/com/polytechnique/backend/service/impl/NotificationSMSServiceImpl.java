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
    private final com.polytechnique.backend.service.InfobipSmsService infobipSmsService;

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
        String patientId = diagnostic.getParametres().getIdentifiantPatient();
        // Extraire la partie après le dernier tiret (ex: P088 de XXX-P088)
        String shortId = patientId.contains("-")
                ? patientId.substring(patientId.lastIndexOf("-") + 1)
                : patientId;

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("AlertePatient %s:\n%s",
                shortId,
                diagnostic.getContenu()));

        if (diagnostic.getRecommandations() != null && !diagnostic.getRecommandations().isEmpty()) {
            messageBuilder.append("\n").append(diagnostic.getRecommandations());
        }

        if (diagnostic.getNiveauUrgence() != null && !diagnostic.getNiveauUrgence().isEmpty()) {
            messageBuilder.append("\n").append(diagnostic.getNiveauUrgence());
        }

        String message = messageBuilder.toString();

        notification.setContenuMessage(message);
        notification.setNumeroDestinataire(infirmier.getTelephone1());
        notification.setInfirmierLocal(infirmier);
        notification.setDiagnostic(diagnostic);

        try {
            infobipSmsService.sendSms(infirmier.getTelephone1(), message);
            notification.setSucces(true);
        } catch (Exception e) {
            notification.setSucces(false);
            // On pourrait logger l'erreur ici, mais elle est déjà loggée dans le service
        }

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
