package com.example.b4subvention.run;

import com.example.b4subvention.repository.ComptableRepository;
import com.example.b4subvention.repository.DepenseRepository;
import com.example.b4subvention.repository.SubventionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Component
@RequiredArgsConstructor
public class Demo1 implements CommandLineRunner {

    private final SubventionRepository subventionRepository;
    private final ComptableRepository comptableRepository;
    private final DepenseRepository depenseRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Liste des subventions
        // subventionRepository.findAll().forEach(System.out::println);

        // Liste des comptables
        // comptableRepository.findAll().forEach(System.out::println);

        // Liste des dépenses
        depenseRepository.listerPourComptableId(1L).forEach(System.out::println);

        System.out.println("===== JPQL avec JOIN =====");
        depenseRepository.listerPourComptableId(1L).forEach(System.out::println);

        System.out.println("===== JPQL sans JOIN =====");
        depenseRepository.listerPourComptableId2(1L).forEach(System.out::println);
        System.out.println("===== Requête dérivée =====");
        depenseRepository.findByComptableIdOrderByDateDepense(1L).forEach(System.out::println);
    }
}