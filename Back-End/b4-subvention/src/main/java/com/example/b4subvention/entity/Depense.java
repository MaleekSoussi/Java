package com.example.b4subvention.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objet;
    private LocalDate dateDepense;
    private BigDecimal montant;
    private BigDecimal reliquat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comptable_id")
   // @ToString.Exclude
    private Comptable comptable;
}