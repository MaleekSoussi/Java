package com.example.demo.dto;

public record AuthResponse(String token, String type, long expiresIn, UtilisateurResponse utilisateur) {
}
