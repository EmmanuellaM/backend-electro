package com.polytechnique.backend.service;

import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.entity.UplinkMessage;
import com.polytechnique.backend.repository.DispositifRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import com.polytechnique.backend.repository.UplinkMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service de traitement des messages uplink LoRaWAN
 * Parse les payloads et crée les Parametres correspondants
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UplinkMessageService {

    private final UplinkMessageRepository uplinkMessageRepository;
    private final ParametresRepository parametresRepository;
    private final DispositifRepository dispositifRepository;

    /**
     * Format du payload attendu:
     * "ID_LOCAL;POIDS;TEMP;SYS;DIA;FCF;GLYC;AGE"
     * Exemple: "P05;65.5;36.8;120;80;142;5.2;28"
     */
    private static final String PAYLOAD_DELIMITER = ";";
    private static final int EXPECTED_FIELDS = 8;

    /**
     * Traite tous les messages non encore traités
     * Appelé périodiquement par le scheduler
     */
    @Scheduled(fixedDelayString = "${uplink.polling.interval:30000}")
    @Transactional
    public void processUnprocessedMessages() {
        List<UplinkMessage> unprocessedMessages = uplinkMessageRepository.findByProcessedFalseOrderByCreatedAtAsc();

        if (!unprocessedMessages.isEmpty()) {
            log.info("Traitement de {} nouveaux messages uplink", unprocessedMessages.size());
        }

        for (UplinkMessage message : unprocessedMessages) {
            try {
                processMessage(message);
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
     * Traite un message uplink individuel
     */
    @Transactional
    public Parametres processMessage(UplinkMessage message) {
        log.debug("Traitement du message ID={}, DevEUI={}", message.getId(), message.getDevEui());

        // 1. Trouver le dispositif par DevEUI
        Optional<Dispositif> dispositifOpt = dispositifRepository.findByDeveui(message.getDevEui());
        if (dispositifOpt.isEmpty()) {
            log.warn("Dispositif non trouvé pour DevEUI={}", message.getDevEui());
            throw new RuntimeException("Dispositif non trouvé pour DevEUI: " + message.getDevEui());
        }
        Dispositif dispositif = dispositifOpt.get();

        // 2. Parser le payload
        String payload = message.getTextPayload();
        if (payload == null || payload.isBlank()) {
            throw new RuntimeException("Payload vide pour le message ID=" + message.getId());
        }

        String[] parts = payload.split(PAYLOAD_DELIMITER);
        if (parts.length < EXPECTED_FIELDS) {
            throw new RuntimeException("Format de payload invalide. Attendu " + EXPECTED_FIELDS
                    + " champs, reçu " + parts.length + ". Payload: " + payload);
        }

        // 3. Extraire les valeurs
        String idLocal = parts[0].trim();
        BigDecimal poids = new BigDecimal(parts[1].trim());
        BigDecimal temperature = new BigDecimal(parts[2].trim());
        int sys = Integer.parseInt(parts[3].trim());
        int dia = Integer.parseInt(parts[4].trim());
        int fcf = Integer.parseInt(parts[5].trim());
        BigDecimal glycemie = new BigDecimal(parts[6].trim());
        int age = Integer.parseInt(parts[7].trim());

        // 4. Générer le codePatientUnique = DevEUI + "-" + ID_LOCAL
        String codePatientUnique = message.getDevEui() + "-" + idLocal;
        log.info("Code patient unique généré: {}", codePatientUnique);

        // 5. Créer l'objet Parametres
        Parametres parametres = new Parametres();
        parametres.setIdentifiantPatient(codePatientUnique);
        parametres.setDispositif(dispositif);
        parametres.setPoidsPatient(poids);
        parametres.setTemperature(temperature);
        parametres.setPressionArterielleSystolique(sys);
        parametres.setPressionArterielleDiastolique(dia);
        parametres.setFrequenceFoetale(fcf);
        parametres.setGlycemie(glycemie);
        parametres.setAgePatient(age);
        parametres.setDateMesure(message.getCreatedAt() != null ? message.getCreatedAt() : LocalDateTime.now());
        parametres.setStatut("en_attente");

        // 6. Sauvegarder
        Parametres saved = parametresRepository.save(parametres);
        log.info("Parametres créés: ID={}, Patient={}", saved.getId(), codePatientUnique);

        return saved;
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
        return uplinkMessageRepository.findByDevEuiOrderByCreatedAtDesc(devEui);
    }
}
