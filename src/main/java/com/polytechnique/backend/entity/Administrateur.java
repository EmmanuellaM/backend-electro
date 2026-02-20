package com.polytechnique.backend.entity;

import com.polytechnique.backend.status.StatutAdministrateur;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un administrateur système
 * Responsable de la création des médecins, infirmiers et dispositifs
 */
@Entity
@Table(name = "administrateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Integer id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Size(max = 150)
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(max = 255)
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.ADMIN;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutAdministrateur statut = StatutAdministrateur.ACTIF;

    @Column(name = "numero_cni", length = 50)
    private String numeroCni;

    @Column(name = "tel", length = 20)
    private String tel;

    @Column(name = "tel2", length = 20)
    private String tel2;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", length = 10)
    private Genre genre;

    @Column(name = "doit_changer_mot_de_passe")
    private Boolean doitChangerMotDePasse = true;

    @Column(name = "patient_lock_timeout", nullable = false)
    private Integer patientLockTimeout = 30;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relations vers les entités créées

    @OneToMany(mappedBy = "administrateur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medecin> medecins = new ArrayList<>();

    @OneToMany(mappedBy = "administrateur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfirmierLocal> infirmiers = new ArrayList<>();

    @OneToMany(mappedBy = "administrateur", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dispositif> dispositifs = new ArrayList<>();
}
