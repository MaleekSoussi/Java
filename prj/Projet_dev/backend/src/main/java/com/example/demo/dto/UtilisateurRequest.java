package com.example.demo.dto;

import jakarta.validation.constraints.*;

public record UtilisateurRequest(
        @NotBlank(message = "Le nom est obligatoire") @Size(max = 100) String nom,
        @NotBlank(message = "Le prénom est obligatoire") @Size(max = 100) String prenom,
        @NotBlank(message = "L'email est obligatoire") @Email(message = "Email invalide") String email,
        // Obligatoire a la creation, facultatif a la modification
        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères") String motDePasse,
        @Pattern(regexp = "PARENT|ADMIN", message = "Le rôle doit être PARENT ou ADMIN") String role,
        @Size(max = 50) String modePaiement,
        @Min(value = 1, message = "Le nombre de paiements doit être au moins 1") Integer nombrePaiements
) {
}
