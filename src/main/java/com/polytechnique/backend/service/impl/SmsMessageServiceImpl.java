package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.dto.request.SmsMessageRequestDTO;
import com.polytechnique.backend.dto.response.SmsMessageResponseDTO;
import com.polytechnique.backend.entity.InfirmierLocal;
import com.polytechnique.backend.entity.SmsMessage;
import com.polytechnique.backend.status.SmsStatus;
import com.polytechnique.backend.exception.ResourceNotFoundException;
import com.polytechnique.backend.repository.InfirmierLocalRepository;
import com.polytechnique.backend.repository.SmsMessageRepository;
import com.polytechnique.backend.service.SmsMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service SMS
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SmsMessageServiceImpl implements SmsMessageService {

    private final SmsMessageRepository smsMessageRepository;
    private final InfirmierLocalRepository infirmierLocalRepository;
    private final com.polytechnique.backend.service.InfobipSmsService infobipSmsService;

    @Override
    public SmsMessageResponseDTO sendSms(SmsMessageRequestDTO requestDTO) {
        // Récupérer l'infirmier
        InfirmierLocal infirmier = infirmierLocalRepository.findById(requestDTO.getInfirmierId())
                .orElseThrow(() -> new ResourceNotFoundException("Infirmier", "id", requestDTO.getInfirmierId()));

        // Créer le message SMS
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setInfirmierLocal(infirmier);
        smsMessage.setTelephone(infirmier.getTelephone1());

        // Préfixer le message
        String fullMessage = "Admin MaterniCare:\n" + requestDTO.getMessage();
        smsMessage.setMessage(fullMessage);

        smsMessage.setSentAt(LocalDateTime.now());
        smsMessage.setSentBy(requestDTO.getSentBy() != null ? requestDTO.getSentBy() : "Admin");

        try {
            // Envoyer le SMS via Infobip
            infobipSmsService.sendSms(infirmier.getTelephone1(), fullMessage);
            smsMessage.setStatus(SmsStatus.SENT);
            log.info("SMS envoyé avec succès à {} ({})", infirmier.getPrenom(), infirmier.getTelephone1());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi du SMS à {}", infirmier.getTelephone1(), e);
            smsMessage.setStatus(SmsStatus.FAILED);
        }

        // Sauvegarder en base
        SmsMessage saved = smsMessageRepository.save(smsMessage);

        return toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmsMessageResponseDTO> getHistoryByInfirmier(int infirmierId) {
        // Vérifier que l'infirmier existe
        if (!infirmierLocalRepository.existsById(infirmierId)) {
            throw new ResourceNotFoundException("Infirmier", "id", infirmierId);
        }

        return smsMessageRepository.findByInfirmierLocalIdOrderBySentAtDesc(infirmierId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByInfirmier(int infirmierId) {
        return smsMessageRepository.countByInfirmierLocalId(infirmierId);
    }

    private SmsMessageResponseDTO toResponseDTO(SmsMessage sms) {
        SmsMessageResponseDTO dto = new SmsMessageResponseDTO();
        dto.setId(sms.getId());
        dto.setInfirmierId(sms.getInfirmierLocal().getId());
        dto.setInfirmierNom(sms.getInfirmierLocal().getPrenom() + " " + sms.getInfirmierLocal().getNom());
        dto.setTelephone(sms.getTelephone());
        dto.setMessage(sms.getMessage());
        dto.setSentAt(sms.getSentAt());
        dto.setSentBy(sms.getSentBy());
        dto.setStatus(sms.getStatus());
        return dto;
    }
}
