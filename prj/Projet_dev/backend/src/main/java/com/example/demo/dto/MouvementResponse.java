package com.example.demo.dto;

import com.example.demo.entity.Mouvement;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MouvementResponse(
        Long id, LocalDate date, BigDecimal montant, String moyenPaiement,
        Long utilisateurId, String utilisateurNom
) {
    public static MouvementResponse from(Mouvement m) {
        return new MouvementResponse(m.getId(), m.getDate(), m.getMontant(), m.getMoyenPaiement(),
                m.getUtilisateur().getId(),
                m.getUtilisateur().getPrenom() + " " + m.getUtilisateur().getNom());
    }
}
