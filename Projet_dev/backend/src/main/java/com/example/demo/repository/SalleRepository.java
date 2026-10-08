package com.example.demo.repository;

import com.example.demo.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {

    boolean existsByNom(String nom);

    // Salles pouvant accueillir au moins N enfants
    List<Salle> findByCapaciteGreaterThanEqual(Integer capacite);
}