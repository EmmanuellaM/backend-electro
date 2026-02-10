package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un dispositif de collecte de données médicales
 * Correspond à la table "dispositif" dans PostgreSQL
 */
@Entity
@Table(name = "dispositif")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dispositif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositif")
    private Integer id;

    @NotBlank(message = "Le DevEUI est obligatoire")
    @Size(max = 50)
    @Column(name = "deveui", nullable = false, unique = true, length = 50)
    private String deveui;

    @NotBlank(message = "L'AppEUI est obligatoire")
    @Size(max = 50)
    @Column(name = "appeui", nullable = false, length = 50)
    private String appeui;

    @NotBlank(message = "L'AppKey est obligatoire")
    @Size(max = 50)
    @Column(name = "appkey", nullable = false, length = 50)
    private String appkey;

    @NotBlank(message = "Le nom du centre de santé est obligatoire")
    @Size(max = 150, message = "Le nom du centre ne peut pas dépasser 150 caractères")
    @Column(name = "nom_centre_de_sante", nullable = false, length = 150)
    private String nomCentreDeSante;

    /**
     * Localisation géographique du centre
     */
    @Size(max = 200, message = "La localisation ne peut pas dépasser 200 caractères")
    @Column(name = "localisation", length = 200)
    private String localisation;

    @Size(max = 50, message = "Le contact ne peut pas dépasser 50 caractères")
    @Column(name = "contact", length = 50)
    private String contact;

    /**
     * Statut du dispositif
     * Valeur par défaut: ACTIF
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 50)
    private StatutDispositif statut = StatutDispositif.NON_ATTRIBUE;

    /**
     * Date d'installation du dispositif
     */
    @Column(name = "date_installation")
    private LocalDate dateInstallation;

    /**
     * Date de création automatique
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Date de dernière mise à jour automatique (trigger)
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Relation One-to-Many avec Parametres
     * Un dispositif peut collecter plusieurs ensembles de paramètres
     */
    @OneToMany(mappedBy = "dispositif", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Parametres> parametres = new ArrayList<>();

    /**
     * Méthode utilitaire pour ajouter des paramètres
     */
    public void addParametres(Parametres param) {
        parametres.add(param);
        param.setDispositif(this);
    }

    /**
     * Méthode utilitaire pour retirer des paramètres
     */
    public void removeParametres(Parametres param) {
        parametres.remove(param);
        param.setDispositif(null);
    }

    /**
     * Relation Many-to-One avec InfirmierLocal
     * Un dispositif est supervisé par un infirmier
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_infirmier_local")
    private InfirmierLocal infirmierLocal;

    /**
     * Administrateur ayant créé ce dispositif
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_administrateur")
    private Administrateur administrateur;
}