package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant un message uplink LoRaWAN
 * Table technique remplie par ChirpStack PostgreSQL Integration
 * LECTURE SEULE - Ne pas modifier cette table depuis l'application
 *
 * Nouvelle structure : les données médicales sont décomposées en colonnes individuelles
 * (plus de parsing de chaîne de caractères)
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

    // -- Informations générales --
    @Column(name = "type")
    private String type;

    // -- Payload - identification application --
    @Column(name = "application_id")
    private String applicationId;

    @Column(name = "application_name")
    private String applicationName;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "dev_eui")
    private String devEui;

    // -- TX Info --
    @Column(name = "frequency")
    private Long frequency;

    @Column(name = "modulation")
    private String modulation;

    // -- LoRa modulation info --
    @Column(name = "bandwidth")
    private Integer bandwidth;

    @Column(name = "spreading_factor")
    private Integer spreadingFactor;

    @Column(name = "code_rate")
    private String codeRate;

    @Column(name = "polarization_inversion")
    private Boolean polarizationInversion;

    // -- Paramètres radio --
    @Column(name = "adr")
    private Boolean adr;

    @Column(name = "dr")
    private Integer dr;

    @Column(name = "f_cnt")
    private Integer fCnt;

    @Column(name = "f_port")
    private Integer fPort;

    @Column(name = "data")
    private String data;

    // -- Données médicales (objectJSON décomposé) --
    @Column(name = "age")
    private Integer age;

    @Column(name = "bpm_moyen")
    private Integer bpmMoyen;

    @Column(name = "ddr")
    private String ddr;

    @Column(name = "fcf")
    private Integer fcf;

    @Column(name = "glycemie", precision = 5, scale = 2)
    private BigDecimal glycemie;

    @Column(name = "id_patient")
    private Integer idPatient;

    @Column(name = "poids", precision = 5, scale = 2)
    private BigDecimal poids;

    @Column(name = "taille", precision = 5, scale = 2)
    private BigDecimal taille;

    @Column(name = "temperature", precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "tension")
    private String tension;

    @Column(name = "tension_dia")
    private Integer tensionDia;

    @Column(name = "tension_sys")
    private Integer tensionSys;

    // -- Autres informations --
    @Column(name = "confirmed_uplink")
    private Boolean confirmedUplink;

    @Column(name = "dev_addr")
    private String devAddr;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "device_profile_id")
    private String deviceProfileId;

    @Column(name = "device_profile_name")
    private String deviceProfileName;

    // -- Champ de traitement interne --
    /**
     * Indique si ce message a déjà été traité par le backend
     */
    @Column(name = "processed")
    private Boolean processed = false;
}
