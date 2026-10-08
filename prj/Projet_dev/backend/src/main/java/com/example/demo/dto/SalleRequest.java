package com.example.demo.dto;

import jakarta.validation.constraints.*;

public record SalleRequest(
        @NotBlank(message = "Le nom est obligatoire") @Size(max = 100) String nom,
        @NotNull(message = "La capacité est obligatoire")
        @Min(value = 1, message = "La capacité doit être au moins 1") Integer capacite
) {
}
