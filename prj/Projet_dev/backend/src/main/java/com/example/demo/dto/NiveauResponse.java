package com.example.demo.dto;

import com.example.demo.entity.Niveau;

import java.math.BigDecimal;

public record NiveauResponse(Long id, String libelle, BigDecimal montant) {
    public static NiveauResponse from(Niveau n) {
        return new NiveauResponse(n.getId(), n.getLibelle(), n.getMontant());
    }
}
