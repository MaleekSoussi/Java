package com.example.b4subvention.run;

import com.example.b4subvention.entity.Imputation;
import com.example.b4subvention.repository.DepenseRepository;
import com.example.b4subvention.repository.ImputationRepository;
import com.example.b4subvention.repository.SubventionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class Demo2Imputation implements CommandLineRunner {

    private final ImputationRepository imputationRepository;
    private final DepenseRepository depenseRepository;
    private final SubventionRepository subventionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Récupération des dépenses et subventions
        var depense1 = depenseRepository.findById(1L).get();
        var depense2 = depenseRepository.findById(2L).get();
        var subvention1 = subventionRepository.findById(1L).get();
        var subvention2 = subventionRepository.findById(2L).get();

        // Création des imputations
        var imputation1 = new Imputation(depense1, subvention1, BigDecimal.valueOf(1000));
        var imputation2 = new Imputation(depense2, subvention2, BigDecimal.valueOf(2000));

        // Enregistrement dans la base
        imputationRepository.save(imputation1);
        imputationRepository.save(imputation2);

        // Liste des imputations
        imputationRepository.findAll().forEach(System.out::println);
    }
}