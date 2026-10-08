package com.example.demo.repository;

import com.example.demo.entity.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {

    List<Cours> findByNiveauId(Long niveauId);

    List<Cours> findBySalleId(Long salleId);

    List<Cours> findByJourOrderByHeureDebut(String jour);

    // Cours où il reste de la place
    List<Cours> findByPlacesDisponiblesGreaterThan(Integer places);

    // Cours proposés à un niveau, avec encore de la place
    List<Cours> findByNiveauIdAndPlacesDisponiblesGreaterThan(Long niveauId, Integer places);

    // Cours auxquels un enfant est inscrit
    List<Cours> findByEnfantsId(Long enfantId);

    // Vérifie si une salle est déjà occupée sur un créneau (pour éviter les chevauchements)
    @Query("""
            SELECT COUNT(c) > 0 FROM Cours c
            WHERE c.salle.id = :salleId
              AND c.jour = :jour
              AND c.heureDebut < :heureFin
              AND c.heureFin > :heureDebut
              AND (:coursId IS NULL OR c.id <> :coursId)
            """)
    boolean salleOccupee(@Param("salleId") Long salleId,
                         @Param("jour") String jour,
                         @Param("heureDebut") LocalTime heureDebut,
                         @Param("heureFin") LocalTime heureFin,
                         @Param("coursId") Long coursId);
}