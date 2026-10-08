package com.example.demo.repository;

import com.example.demo.entity.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {

    Optional<Niveau> findByLibelle(String libelle);

    boolean existsByLibelle(String libelle);
}