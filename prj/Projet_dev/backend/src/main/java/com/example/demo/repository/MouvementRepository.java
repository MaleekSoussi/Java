package com.example.demo.repository;

import com.example.demo.entity.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MouvementRepository extends JpaRepository<Mouvement, Long> {

    List<Mouvement> findByUtilisateurIdOrderByDateDescIdDesc(Long utilisateurId);

    List<Mouvement> findAllByOrderByDateDescIdDesc();

    // Total deja paye par un utilisateur (0 s'il n'a rien paye)
    @Query("SELECT COALESCE(SUM(m.montant), 0) FROM Mouvement m WHERE m.utilisateur.id = :utilisateurId")
    BigDecimal totalPaye(@Param("utilisateurId") Long utilisateurId);
}
