package com.polytechnique.backend.service;

import com.polytechnique.backend.entity.Parametres;
import com.polytechnique.backend.repository.ParametresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Tâche planifiée qui libère automatiquement les verrous de dossiers patients
 * expirés.
 *
 * Règle métier : un médecin ne peut pas bloquer un dossier plus longtemps que
 * le délai défini par l'administrateur (patientLockTimeout en minutes).
 * Si le délai est dépassé et que le médecin n'est plus actif, le dossier est
 * automatiquement rendu disponible pour ses confrères.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LockCleanupScheduler {

    private final ParametresRepository parametresRepository;
    private final SseService sseService;

    /**
     * S'exécute toutes les 30 secondes pour libérer les verrous expirés.
     * Gère correctement les délais différents par administrateur.
     */
    @Scheduled(fixedDelay = 30_000) // toutes les 30 secondes
    @Transactional
    public void releaseExpiredLocks() {
        // Récupère tous les dossiers verrouillés
        List<Parametres> locked = parametresRepository.findAllLocked();
        if (locked.isEmpty())
            return;

        LocalDateTime now = LocalDateTime.now();
        int released = 0;

        for (Parametres p : locked) {
            if (p.getVerrouilleAt() == null)
                continue;

            // Récupérer le délai configuré par l'admin (défaut 30 min)
            int timeoutMinutes = 30;
            try {
                if (p.getDispositif() != null && p.getDispositif().getAdministrateur() != null) {
                    timeoutMinutes = p.getDispositif().getAdministrateur().getPatientLockTimeout();
                }
            } catch (Exception e) {
                log.warn("Impossible de récupérer le timeout admin pour parametres#{}, utilisation du délai par défaut",
                        p.getId());
            }

            // Vérifier si le verrou a expiré
            LocalDateTime expiry = p.getVerrouilleAt().plusMinutes(timeoutMinutes);
            if (expiry.isBefore(now)) {
                log.info("Libération automatique du dossier parametres#{} (verrouillé par médecin#{} depuis {}min+{})",
                        p.getId(), p.getVerrouilleParMedecinId(), timeoutMinutes,
                        java.time.Duration.between(expiry, now).toMinutes());
                p.setVerrouilleParMedecinId(null);
                p.setVerrouilleAt(null);
                parametresRepository.save(p);

                // Notifier les clients connectés qu'un dossier s'est libéré
                sseService.broadcast("LOCK_RELEASED", p.getIdentifiantPatient());
                released++;
            }
        }

        if (released > 0) {
            log.info("Nettoyage des verrous: {} dossier(s) libéré(s) automatiquement", released);
        }
    }
}
