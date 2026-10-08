package com.example.demo.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EnfantRequest(
        @NotBlank(message = "Le nom est obligatoire") @Size(max = 100) String nom,
        @NotBlank(message = "Le prénom est obligatoire") @Size(max = 100) String prenom,
        @Past(message = "La date de naissance doit être dans le passé") LocalDate dateNaissance,
        @Size(max = 150) String etablissement,
        Long niveauId,
        // Utilise uniquement par l'admin (un parent ajoute toujours ses propres enfants)
        Long utilisateurId
) {
}
