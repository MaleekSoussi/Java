package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "mode_paiement", length = 50)
    private String modePaiement;

    @Column(name = "nombre_paiements")
    private Integer nombrePaiements;

    @Column(name = "solde_restant", precision = 10, scale = 2)
    private BigDecimal soldeRestant = BigDecimal.ZERO;

    // Un parent a un ou plusieurs enfants
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enfant> enfants = new ArrayList<>();

    // Un utilisateur a plusieurs mouvements (paiements)
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mouvement> mouvements = new ArrayList<>();

    public Utilisateur() {
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public Integer getNombrePaiements() { return nombrePaiements; }
    public void setNombrePaiements(Integer nombrePaiements) { this.nombrePaiements = nombrePaiements; }

    public BigDecimal getSoldeRestant() { return soldeRestant; }
    public void setSoldeRestant(BigDecimal soldeRestant) { this.soldeRestant = soldeRestant; }

    public List<Enfant> getEnfants() { return enfants; }
    public void setEnfants(List<Enfant> enfants) { this.enfants = enfants; }

    public List<Mouvement> getMouvements() { return mouvements; }
    public void setMouvements(List<Mouvement> mouvements) { this.mouvements = mouvements; }
}
