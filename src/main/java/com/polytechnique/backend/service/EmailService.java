package com.polytechnique.backend.service;

/**
 * Service pour l'envoi d'emails
 */
public interface EmailService {

    /**
     * Envoie un email contenant le code de réinitialisation de mot de passe
     * 
     * @param to   Adresse email du destinataire
     * @param code Code de vérification à 6 chiffres
     */
    void sendPasswordResetCode(String to, String code);
}
