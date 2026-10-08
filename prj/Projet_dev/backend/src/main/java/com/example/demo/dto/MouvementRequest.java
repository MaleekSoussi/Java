package com.example.demo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MouvementRequest(
        @NotNull(message = "Le montant est obligatoire")
        @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0") BigDecimal montant,
        @NotBlank(message = "Le moyen de paiement est obligatoire") @Size(max = 50) String moyenPaiement,
        LocalDate date,
        // Utilise uniquement par l'admin (un parent paie toujours pour lui-meme)
        Long utilisateurId
) {
}
