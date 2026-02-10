package com.polytechnique.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité Spring Security
 * - Désactive CSRF pour l'API REST
 * - Autorise toutes les requêtes (API publique pour l'instant)
 * - Fournit un PasswordEncoder BCrypt pour le hashage des mots de passe
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean PasswordEncoder utilisant BCrypt
     * Force de hashage par défaut : 10 rounds
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration de la chaîne de sécurité
     * Pour l'instant, on autorise toutes les requêtes sans authentification
     * TODO: Implémenter JWT pour sécuriser les endpoints
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }
}
