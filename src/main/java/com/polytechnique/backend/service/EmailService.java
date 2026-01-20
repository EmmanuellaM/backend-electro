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

    /**
     * Envoie un email de bienvenue avec les identifiants de connexion
     * 
     * @param to       Adresse email du destinataire
     * @param nom      Nom complet du médecin
     * @param password Mot de passe généré
     */
    void sendNewAccountEmail(String to, String nom, String password);
}
