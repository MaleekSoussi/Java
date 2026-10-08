package com.example.b4subvention.repository;

import com.example.b4subvention.entity.Comptable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComptableRepository extends JpaRepository<Comptable, Long> {
}