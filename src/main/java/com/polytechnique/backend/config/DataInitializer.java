package com.polytechnique.backend.config;

import com.polytechnique.backend.entity.Administrateur;
import com.polytechnique.backend.entity.Role;
import com.polytechnique.backend.repository.AdministrateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final AdministrateurRepository administrateurRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            String superAdminEmail = "superadmin@maternicare.com";
            if (!administrateurRepository.existsByEmail(superAdminEmail)) {
                log.info("Super Admin non trouvé. Création en cours...");

                Administrateur superAdmin = new Administrateur();
                superAdmin.setNom("Super Admin");
                superAdmin.setEmail(superAdminEmail);
                superAdmin.setMotDePasse("SuperAdminPass1!"); // In production, this should be encoded
                superAdmin.setRole(Role.SUPER_ADMIN);
                superAdmin.setCreatedAt(LocalDateTime.now());
                superAdmin.setUpdatedAt(LocalDateTime.now());

                administrateurRepository.save(superAdmin);
                log.info("Super Admin créé avec succès !");
            } else {
                log.info("Super Admin existe déjà.");
            }
        };
    }
}
