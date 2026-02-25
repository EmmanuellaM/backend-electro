package com.polytechnique.backend.service;

import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.entity.UplinkMessage;
import com.polytechnique.backend.repository.DispositifRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import com.polytechnique.backend.repository.UplinkMessageRepository;
import com.polytechnique.backend.status.StatutParametre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Service de traitement des messages uplink LoRaWAN
 * Lit directement les colonnes médicales depuis UplinkMessage
 * et crée les Parametres correspondants
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UplinkMessageService {

    private final UplinkMessageRepository uplinkMessageRepository;
    private final UplinkProcessingService uplinkProcessingService;

    /**
     * Traite tous les messages non encore traités
     * Appelé périodiquement par le scheduler
     */
    @Scheduled(fixedDelayString = "${uplink.polling.interval:30000}")
    public void processUnprocessedMessages() {
        List<UplinkMessage> unprocessedMessages = uplinkMessageRepository.findByProcessedFalseOrderByPublishedAtAsc();

        if (!unprocessedMessages.isEmpty()) {
            log.info("Traitement de {} nouveaux messages uplink", unprocessedMessages.size());
        }

        for (UplinkMessage message : unprocessedMessages) {
            try {
                uplinkProcessingService.processMessage(message);
                message.setProcessed(true);
                uplinkMessageRepository.save(message);
            } catch (Exception e) {
                log.error("Erreur lors du traitement du message ID={}: {}", message.getId(), e.getMessage());
                // Marquer comme traité pour éviter les boucles infinies
                message.setProcessed(true);
                uplinkMessageRepository.save(message);
            }
        }
    }

    /**
     * Récupère le nombre de messages non traités
     */
    public long getUnprocessedCount() {
        return uplinkMessageRepository.countByProcessedFalse();
    }

    /**
     * Récupère les derniers messages d'un dispositif
     */
    public List<UplinkMessage> getMessagesByDevEui(String devEui) {
        return uplinkMessageRepository.findByDevEuiOrderByPublishedAtDesc(devEui);
    }
}
