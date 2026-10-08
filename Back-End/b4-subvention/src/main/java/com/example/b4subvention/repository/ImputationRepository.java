package com.example.b4subvention.repository;

import com.example.b4subvention.entity.Imputation;
import com.example.b4subvention.entity.ImputationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImputationRepository extends JpaRepository<Imputation, ImputationId> {
}