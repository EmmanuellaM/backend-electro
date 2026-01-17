package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité représentant un message uplink LoRaWAN
 * Table technique remplie par ChirpStack PostgreSQL Integration
 * LECTURE SEULE - Ne pas modifier cette table depuis l'application
 */
@Entity
@Table(name = "uplink_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UplinkMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "application_id")
    private String applicationId;

    @Column(name = "application_name")
    private String applicationName;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_profile_name")
    private String deviceProfileName;

    @Column(name = "device_profile_id")
    private UUID deviceProfileId;

    /**
     * DevEUI du dispositif (identifiant unique du boîtier)
     * Utilisé pour lier le message au Dispositif
     */
    @Column(name = "dev_eui")
    private String devEui;

    @Column(name = "frequency")
    private Long frequency;

    @Column(name = "dr")
    private Integer dr;

    @Column(name = "adr")
    private Boolean adr;

    @Column(name = "f_cnt")
    private Integer fCnt;

    @Column(name = "f_port")
    private Integer fPort;

    /**
     * Données en Base64 (payload brut)
     */
    @Column(name = "data_base64")
    private String dataBase64;

    /**
     * Payload décodé en texte
     * Format attendu: "ID_LOCAL;POIDS;TEMP;SYS;DIA;FCF;GLYC;AGE"
     * Exemple: "P05;65.5;36.8;120;80;142;5.2;28"
     */
    @Column(name = "text_payload")
    private String textPayload;

    /**
     * Date de réception du message par ChirpStack
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Indique si ce message a déjà été traité par le backend
     */
    @Column(name = "processed")
    private Boolean processed = false;
}
