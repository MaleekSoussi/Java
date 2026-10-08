package com.example.demo.dto;

import jakarta.validation.constraints.*;

import java.time.LocalTime;

public record CoursRequest(
        @NotBlank(message = "Le jour est obligatoire")
        @Pattern(regexp = "LUNDI|MARDI|MERCREDI|JEUDI|VENDREDI|SAMEDI|DIMANCHE", message = "Jour invalide")
        String jour,
        @NotNull(message = "L'heure de début est obligatoire") LocalTime heureDebut,
        @NotNull(message = "L'heure de fin est obligatoire") LocalTime heureFin,
        @NotNull(message = "Le nombre de places est obligatoire")
        @Min(value = 0, message = "Le nombre de places ne peut pas être négatif") Integer placesDisponibles,
        Long salleId,
        Long niveauId
) {
}
