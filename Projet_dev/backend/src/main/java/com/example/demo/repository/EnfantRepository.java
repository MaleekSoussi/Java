package com.example.demo.repository;

import com.example.demo.entity.Enfant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnfantRepository extends JpaRepository<Enfant, Long> {

    // Enfants d'un parent
    List<Enfant> findByUtilisateurId(Long utilisateurId);

    // Enfants d'un niveau
    List<Enfant> findByNiveauId(Long niveauId);

    // Enfants inscrits à un cours
    List<Enfant> findByCoursId(Long coursId);

    // Nombre d'inscrits à un cours
    long countByCoursId(Long coursId);
}