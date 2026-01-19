package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un médecin
 * Correspond à la table "medecin" dans PostgreSQL
 */
@Entity
@Table(name = "medecin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medecin")
    private Integer id;

    @NotBlank(message = "Le nom du médecin est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le prénom du médecin est obligatoire")
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @NotBlank(message = "L'email du médecin est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 150, message = "L'email ne peut pas dépasser 150 caractères")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Size(max = 20, message = "Le téléphone ne peut pas dépasser 20 caractères")
    @Column(name = "tel", length = 20)
    private String tel;

    /**
     * Mot de passe du médecin pour l'accès à la plateforme
     */
    @Column(name = "mot_de_passe")
    private String motDePasse;

    /**
     * Numéro de Carte Nationale d'Identité
     */
    @NotBlank(message = "Le CNI est obligatoire")
    @Size(max = 50)
    @Column(name = "numero_carte_identite", nullable = false, unique = true)
    private String numeroCarteIdentite;

    /**
     * Statut du médecin (ACTIF, INACTIF, SUSPENDU)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutMedecin statut = StatutMedecin.ACTIF;

    /**
     * Genre du médecin
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    /**
     * Spécialité médicale du médecin
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "specialite")
    private SpecialiteMedecin specialite;

    @Column(name = "date_inscription")
    private LocalDateTime dateInscription = LocalDateTime.now();

    @Column(name = "derniere_connexion")
    private LocalDateTime derniereConnexion;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Relation One-to-Many avec Diagnostic
     * Un médecin peut établir plusieurs diagnostics
     */
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Diagnostic> diagnostics = new ArrayList<>();

    @org.hibernate.annotations.Formula("(SELECT count(*) FROM diagnostic d WHERE d.id_medecin = id_medecin)")
    private Integer nombreDiagnosticsCalculated;

    /**
     * Administrateur ayant créé ce médecin
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_administrateur")
    private Administrateur administrateur;

    /**
     * Méthode utilitaire pour ajouter un diagnostic
     */
    public void addDiagnostic(Diagnostic diagnostic) {
        diagnostics.add(diagnostic);
        diagnostic.setMedecin(this);
    }

    /**
     * Méthode utilitaire pour retirer un diagnostic
     */
    public void removeDiagnostic(Diagnostic diagnostic) {
        diagnostics.remove(diagnostic);
        diagnostic.setMedecin(null);
    }
}