package com.example.demo.dto;

import com.example.demo.entity.Utilisateur;

import java.math.BigDecimal;

// Jamais de mot de passe dans une reponse
public record UtilisateurResponse(
        Long id, String nom, String prenom, String email, String role,
        String modePaiement, Integer nombrePaiements, BigDecimal soldeRestant
) {
    public static UtilisateurResponse from(Utilisateur u) {
        return new UtilisateurResponse(u.getId(), u.getNom(), u.getPrenom(), u.getEmail(), u.getRole(),
                u.getModePaiement(), u.getNombrePaiements(), u.getSoldeRestant());
    }
}
