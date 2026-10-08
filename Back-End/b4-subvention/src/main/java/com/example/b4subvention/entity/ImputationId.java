package com.example.b4subvention.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImputationId implements Serializable {

    private Long depense;
    private Long subvention;
}