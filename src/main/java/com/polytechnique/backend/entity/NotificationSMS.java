package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant une notification SMS envoyée à un infirmier local.
 * Générée lors de la validation d'un diagnostic.
 */
@Entity
@Table(name = "notification_sms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSMS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Integer id;

    @NotBlank(message = "Le contenu du message est obligatoire")
    @Column(name = "contenu_message", nullable = false, columnDefinition = "TEXT")
    private String contenuMessage;

    @NotBlank(message = "Le numéro du destinataire est obligatoire")
    @Column(name = "numero_destinataire", nullable = false, length = 20)
    private String numeroDestinataire;

    @CreationTimestamp
    @Column(name = "date_envoi", updatable = false)
    private LocalDateTime dateEnvoi;

    @Column(name = "succes")
    private Boolean succes = false;

    /**
     * Relation Many-to-One avec InfirmierLocal
     * Un SMS est envoyé à un infirmier spécifique
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_infirmier_local")
    private InfirmierLocal infirmierLocal;

    /**
     * Relation One-to-One optionnelle avec Diagnostic
     * Lien de traçabilité pour savoir quel diagnostic a généré ce SMS
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_diagnostic")
    private Diagnostic diagnostic;
}
