package com.example.demo.dto;

import com.example.demo.entity.Cours;

import java.math.BigDecimal;
import java.time.LocalTime;

public record CoursResponse(
        Long id, String jour, LocalTime heureDebut, LocalTime heureFin, Integer placesDisponibles,
        Long salleId, String salleNom, Long niveauId, String niveauLibelle, BigDecimal montant,
        int nombreInscrits
) {
    public static CoursResponse from(Cours c) {
        return new CoursResponse(
                c.getId(), c.getJour(), c.getHeureDebut(), c.getHeureFin(), c.getPlacesDisponibles(),
                c.getSalle() != null ? c.getSalle().getId() : null,
                c.getSalle() != null ? c.getSalle().getNom() : null,
                c.getNiveau() != null ? c.getNiveau().getId() : null,
                c.getNiveau() != null ? c.getNiveau().getLibelle() : null,
                c.getNiveau() != null ? c.getNiveau().getMontant() : null,
                c.getEnfants().size()
        );
    }
}
