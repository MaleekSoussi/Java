package com.example.b4subvention.repository;

import com.example.b4subvention.entity.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {

    @Query("SELECT d FROM Depense d JOIN d.comptable c WHERE c.id = :comptableId ORDER BY d.dateDepense")
    List<Depense> listerPourComptableId(Long comptableId);

    @Query("SELECT d FROM Depense d WHERE d.comptable.id = :comptableId ORDER BY d.dateDepense")
    List<Depense> listerPourComptableId2(Long comptableId);

    List<Depense> findByComptableIdOrderByDateDepense(Long comptableId);
}