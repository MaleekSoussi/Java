package com.example.b4subvention.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subvention {

    @Id


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int annee;
    private String organisme;
    private String nom;
    private BigDecimal montant;
    private BigDecimal disponible;
}