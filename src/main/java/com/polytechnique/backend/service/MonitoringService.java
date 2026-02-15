package com.polytechnique.backend.service;

import com.polytechnique.backend.entity.Dispositif;
import com.polytechnique.backend.entity.InfirmierLocal;
import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.repository.DispositifRepository;
import com.polytechnique.backend.repository.InfirmierLocalRepository;
import com.polytechnique.backend.repository.ParametresRepository;
import com.polytechnique.backend.status.StatutDispositif;
import com.polytechnique.backend.status.StatutInfirmier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service de surveillance automatique des statuts
 * Gère la règle des 7 jours d'inactivité pour les dispositifs et les infirmiers
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MonitoringService {

    private final DispositifRepository dispositifRepository;
    private final InfirmierLocalRepository infirmierLocalRepository;
    private final ParametresRepository parametresRepository;

    /**
     * Vérifie l'inactivité des dispositifs et des infirmiers tous les jours
     * (ou plus fréquemment en développement, ici toutes les heures pour être
     * réactif)
     */
    @Scheduled(cron = "0 0 * * * *") // Toutes les heures
    @Transactional
    public void checkInactivity() {
        log.info("Démarrage de la vérification automatique de l'inactivité (Règle des 7 jours)");

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // 1. Vérifier les dispositifs
        List<Dispositif> activeDevices = dispositifRepository.findAll().stream()
                .filter(d -> d.getStatut() == StatutDispositif.ACTIF)
                .toList();

        for (Dispositif device : activeDevices) {
            List<Parametres> latestParams = parametresRepository.findByDispositifIdOrderByIdDesc(device.getId());

            boolean inactive = false;
            if (latestParams.isEmpty()) {
                // Si aucune donnée depuis l'installation (et installé il y a plus de 7 jours)
                if (device.getCreatedAt().isBefore(sevenDaysAgo)) {
                    inactive = true;
                }
            } else {
                Parametres latest = latestParams.get(0);
                if (latest.getDateMesure().isBefore(sevenDaysAgo)) {
                    inactive = true;
                }
            }

            if (inactive) {
                log.info("Dispositif {} (DevEUI: {}) marqué comme INACTIF par inactivité", device.getId(),
                        device.getDeveui());
                device.setStatut(StatutDispositif.INACTIF);
                dispositifRepository.save(device);
            }
        }

        // 2. Vérifier les infirmiers
        List<InfirmierLocal> activeNurses = infirmierLocalRepository.findAll().stream()
                .filter(i -> i.getStatut() == StatutInfirmier.ACTIF)
                .toList();

        for (InfirmierLocal nurse : activeNurses) {
            List<Dispositif> nurseDevices = nurse.getDispositifs();

            if (nurseDevices.isEmpty()) {
                // Si l'infirmier n'a pas de dispositif, on pourrait décider de le laisser actif
                // ou non.
                // Selon la règle "ne passe à actif que quand il emmet", on pourrait le passer
                // inactif.
                continue;
            }

            boolean hasActiveDevice = false;
            for (Dispositif device : nurseDevices) {
                // Si au moins un dispositif a émis récemment
                List<Parametres> latestParams = parametresRepository.findByDispositifIdOrderByIdDesc(device.getId());
                if (!latestParams.isEmpty()) {
                    if (latestParams.get(0).getDateMesure().isAfter(sevenDaysAgo)) {
                        hasActiveDevice = true;
                        break;
                    }
                }
            }

            if (!hasActiveDevice) {
                log.info("Infirmier {} ({}) marqué comme INACTIF par inactivité de ses dispositifs", nurse.getId(),
                        nurse.getNom());
                nurse.setStatut(StatutInfirmier.INACTIF);
                infirmierLocalRepository.save(nurse);
            }
        }
    }
}
