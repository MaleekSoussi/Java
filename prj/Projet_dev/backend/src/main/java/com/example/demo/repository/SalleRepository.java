package com.example.demo.repository;

import com.example.demo.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalleRepository extends JpaRepository<Salle, Long> {

    boolean existsByNomIgnoreCase(String nom);

    List<Salle> findAllByOrderByNomAsc();
}
