package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant les seuils de maintenance des dispositifs.
 * Il n'existe qu'un seul enregistrement (singleton en base) identifié par id=1.
 * Lorsqu'une mesure dépasse ces seuils, le dispositif passe en statut
 * MAINTENANCE.
 */
@Entity
@Table(name = "seuil_maintenance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeuilMaintenance {

    @Id
    @Column(name = "id")
    private Integer id = 1;

    @Column(name = "temperature_min", nullable = false, precision = 4, scale = 1)
    private BigDecimal temperatureMin = new BigDecimal("35.5");

    @Column(name = "temperature_max", nullable = false, precision = 4, scale = 1)
    private BigDecimal temperatureMax = new BigDecimal("38.5");

    @Column(name = "frequence_foetale_min", nullable = false)
    private Integer frequenceFoetaleMin = 110;

    @Column(name = "frequence_foetale_max", nullable = false)
    private Integer frequenceFoetaleMax = 160;

    @Column(name = "frequence_cardiaque_mere_min", nullable = false)
    private Integer frequenceCardiaqueMereMin = 60;

    @Column(name = "frequence_cardiaque_mere_max", nullable = false)
    private Integer frequenceCardiaqueMereMax = 100;

    @Column(name = "pression_systolique_max", nullable = false)
    private Integer pressionSystoliqueMax = 140;

    @Column(name = "pression_diastolique_max", nullable = false)
    private Integer pressionDiastoliqueMax = 90;

    @Column(name = "glycemie_max", nullable = false, precision = 4, scale = 1)
    private BigDecimal glycemieMax = new BigDecimal("7.0");

    @Column(name = "saturation_oxygene_min", nullable = false)
    private Integer saturationOxygeneMin = 95;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
