package com.example.demo.repository;

import com.example.demo.entity.Enfant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EnfantRepository extends JpaRepository<Enfant, Long> {

    List<Enfant> findByUtilisateurIdOrderByPrenomAsc(Long utilisateurId);

    List<Enfant> findAllByOrderByNomAscPrenomAsc();

    List<Enfant> findByNiveauId(Long niveauId);

    List<Enfant> findByCoursIdOrderByNomAsc(Long coursId);

    // Montant total du par un parent : prix du niveau de chaque cours suivi par ses enfants
    @Query("""
            SELECT COALESCE(SUM(n.montant), 0)
            FROM Enfant e JOIN e.cours c JOIN c.niveau n
            WHERE e.utilisateur.id = :utilisateurId
            """)
    BigDecimal totalDu(@Param("utilisateurId") Long utilisateurId);
}
