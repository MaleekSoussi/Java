package com.example.demo.dto;

import com.example.demo.entity.Enfant;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public record EnfantResponse(
        Long id, String nom, String prenom, LocalDate dateNaissance, String etablissement,
        Long niveauId, String niveauLibelle,
        Long utilisateurId, String parentNom,
        List<CoursResponse> cours
) {
    private static final List<String> JOURS =
            List.of("LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "DIMANCHE");

    public static EnfantResponse from(Enfant e) {
        List<CoursResponse> cours = e.getCours().stream()
                .map(CoursResponse::from)
                .sorted(Comparator.comparing((CoursResponse c) -> JOURS.indexOf(c.jour()))
                        .thenComparing(CoursResponse::heureDebut))
                .toList();
        return new EnfantResponse(
                e.getId(), e.getNom(), e.getPrenom(), e.getDateNaissance(), e.getEtablissement(),
                e.getNiveau() != null ? e.getNiveau().getId() : null,
                e.getNiveau() != null ? e.getNiveau().getLibelle() : null,
                e.getUtilisateur().getId(),
                e.getUtilisateur().getPrenom() + " " + e.getUtilisateur().getNom(),
                cours
        );
    }
}
