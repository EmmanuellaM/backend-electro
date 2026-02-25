package com.polytechnique.backend.service;

import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.entity.UplinkMessage;
import com.polytechnique.backend.repository.DispositifRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import com.polytechnique.backend.status.StatutParametre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UplinkProcessingService {

    private final ParametresRepository parametresRepository;
    private final DispositifRepository dispositifRepository;
    private final SseService sseService;
    private final SeuilMaintenanceService seuilMaintenanceService;

    @Transactional
    public Parametres processMessage(UplinkMessage message) {
        log.debug("Traitement du message ID={}, DevEUI={}", message.getId(), message.getDevEui());

        // 1. Trouver le dispositif par DevEUI
        Optional<Dispositif> dispositifOpt = dispositifRepository.findByDeveui(message.getDevEui());
        if (dispositifOpt.isEmpty()) {
            throw new RuntimeException("Dispositif non trouvé pour DevEUI: " + message.getDevEui());
        }
        Dispositif dispositif = dispositifOpt.get();

        // 2. Vérifier que l'id_patient est présent
        if (message.getIdPatient() == null) {
            throw new RuntimeException("id_patient manquant dans le message ID=" + message.getId());
        }

        // 3. Générer le codePatientUnique
        String codePatientUnique = message.getDevEui() + "-" + message.getIdPatient();

        // 4. Créer l'objet Parametres
        Parametres parametres = new Parametres();
        parametres.setIdentifiantPatient(codePatientUnique);
        parametres.setDispositif(dispositif);
        parametres.setPoidsPatient(message.getPoids());
        parametres.setTaillePatient(message.getTaille());
        parametres.setTemperature(message.getTemperature());
        parametres.setPressionArterielleSystolique(message.getTensionSys());
        parametres.setPressionArterielleDiastolique(message.getTensionDia());
        parametres.setFrequenceFoetale(message.getFcf());
        parametres.setGlycemie(message.getGlycemie());
        parametres.setAgePatient(message.getAge());
        parametres.setFrequenceCardiaqueMere(message.getBpmMoyen());

        if (message.getDdr() != null && !message.getDdr().isBlank()) {
            try {
                parametres.setDateDernieresRegles(LocalDate.parse(message.getDdr()));
            } catch (Exception e) {
                try {
                    parametres.setDateDernieresRegles(
                            LocalDate.parse(message.getDdr(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } catch (Exception e2) {
                    log.warn("Impossible de parser la DDR: '{}'", message.getDdr());
                }
            }
        }

        parametres.setDateMesure(message.getPublishedAt() != null ? message.getPublishedAt() : LocalDateTime.now());

        // Archivage automatique
        List<Parametres> mesuresEnAttente = parametresRepository.findByIdentifiantPatientAndStatut(
                codePatientUnique,
                StatutParametre.EN_ATTENTE);
        if (!mesuresEnAttente.isEmpty()) {
            mesuresEnAttente.forEach(p -> p.setStatut(StatutParametre.ARCHIVE));
            parametresRepository.saveAll(mesuresEnAttente);
        }

        parametres.setStatut(StatutParametre.EN_ATTENTE);

        // Activer dispositif et infirmier
        if (dispositif.getStatut() != com.polytechnique.backend.status.StatutDispositif.ACTIF &&
                dispositif.getStatut() != com.polytechnique.backend.status.StatutDispositif.SUPPRIME) {
            dispositif.setStatut(com.polytechnique.backend.status.StatutDispositif.ACTIF);
        }

        if (dispositif.getInfirmierLocal() != null) {
            com.polytechnique.backend.entity.InfirmierLocal inf = dispositif.getInfirmierLocal();
            if (inf.getStatut() != com.polytechnique.backend.status.StatutInfirmier.ACTIF &&
                    inf.getStatut() != com.polytechnique.backend.status.StatutInfirmier.SUPPRIME) {
                inf.setStatut(com.polytechnique.backend.status.StatutInfirmier.ACTIF);
            }
        }

        Parametres saved = parametresRepository.save(parametres);
        log.info("Parametres créés: ID={}, Patient={}, Statut={}", saved.getId(), codePatientUnique, saved.getStatut());

        seuilMaintenanceService.checkMaintenance(saved);

        // Envoyer SSE après commit (via thread séparé)
        String patientIdForSse = saved.getIdentifiantPatient();
        new Thread(() -> {
            try {
                Thread.sleep(800);
                sseService.broadcast("NEW_PATIENT_DATA", patientIdForSse);
                log.info("SSE broadcast envoyé pour le patient: {}", patientIdForSse);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        return saved;
    }
}
