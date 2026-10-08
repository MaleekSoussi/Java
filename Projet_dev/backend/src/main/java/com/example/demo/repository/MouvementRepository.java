package com.example.demo.repository;

import com.example.demo.entity.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MouvementRepository extends JpaRepository<Mouvement, Long> {

    // Historique des paiements d'un utilisateur, du plus récent au plus ancien
    List<Mouvement> findByUtilisateurIdOrderByDateDesc(Long utilisateurId);

    // Paiements sur une période
    List<Mouvement> findByDateBetween(LocalDate debut, LocalDate fin);

    // Total déjà payé par un utilisateur (0 s'il n'a encore rien payé)
    @Query("SELECT COALESCE(SUM(m.montant), 0) FROM Mouvement m WHERE m.utilisateur.id = :utilisateurId")
    BigDecimal totalPayeParUtilisateur(@Param("utilisateurId") Long utilisateurId);
}