package com.example.demo.dto;

public record AuthResponse(
        String token,
        String type,
        long expiresIn,
        com.example.demo.dto.UtilisateurResponse utilisateur
) {
}