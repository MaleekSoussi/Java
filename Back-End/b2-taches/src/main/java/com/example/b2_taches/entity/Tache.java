package com.example.b2_taches.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tache {
    private Long id;
    private String titre;
    private boolean faite;
}