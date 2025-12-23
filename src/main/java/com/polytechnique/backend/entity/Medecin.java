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
 * Entité représentant un médecin dans le système
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

    /**
     * Mot de passe hashé (BCrypt)
     * Valeur par défaut temporaire: "password123"
     */
    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse = "password123";

    @Size(max = 20, message = "Le téléphone ne peut pas dépasser 20 caractères")
    @Column(name = "tel", length = 20)
    private String tel;

    /**
     * Statut du médecin: actif, inactif, suspendu
     * Valeur par défaut: "actif"
     */
    @Column(name = "statut", length = 20)
    private String statut = "actif";

    /**
     * Rôle du médecin: "admin" ou "medecin"
     * Valeur par défaut: "medecin"
     */
    @Column(name = "role", length = 20)
    private String role = "medecin";

    /**
     * Date d'inscription automatique
     */
    @CreationTimestamp
    @Column(name = "date_inscription", updatable = false)
    private LocalDateTime dateInscription;

    /**
     * Date de dernière connexion
     * Mise à jour manuellement lors de la connexion
     */
    @Column(name = "derniere_connexion")
    private LocalDateTime derniereConnexion;

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
     * Relation One-to-Many avec Diagnostic
     * Un médecin peut établir plusieurs diagnostics
     */
    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Diagnostic> diagnostics = new ArrayList<>();

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

    // Manual Getters and Setters to bypass Lombok issues
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public java.time.LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(java.time.LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public java.time.LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(java.time.LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(List<Diagnostic> diagnostics) {
        this.diagnostics = diagnostics;
    }

}