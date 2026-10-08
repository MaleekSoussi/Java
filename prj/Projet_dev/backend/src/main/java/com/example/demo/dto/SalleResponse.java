package com.example.demo.dto;

import com.example.demo.entity.Salle;

public record SalleResponse(Long id, String nom, Integer capacite) {
    public static SalleResponse from(Salle s) {
        return new SalleResponse(s.getId(), s.getNom(), s.getCapacite());
    }
}
