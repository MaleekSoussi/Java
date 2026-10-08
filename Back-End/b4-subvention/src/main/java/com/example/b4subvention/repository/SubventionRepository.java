package com.example.b4subvention.repository;

import com.example.b4subvention.entity.Subvention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubventionRepository extends JpaRepository<Subvention, Long> {
}