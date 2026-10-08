package com.example.demo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record NiveauRequest(
        @NotBlank(message = "Le libellé est obligatoire") @Size(max = 100) String libelle,
        @NotNull(message = "Le montant est obligatoire")
        @DecimalMin(value = "0", message = "Le montant doit être positif") BigDecimal montant
) {
}
