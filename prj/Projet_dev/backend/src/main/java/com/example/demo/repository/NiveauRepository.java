package com.example.demo.repository;

import com.example.demo.entity.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NiveauRepository extends JpaRepository<Niveau, Long> {

    boolean existsByLibelleIgnoreCase(String libelle);

    List<Niveau> findAllByOrderByLibelleAsc();
}
