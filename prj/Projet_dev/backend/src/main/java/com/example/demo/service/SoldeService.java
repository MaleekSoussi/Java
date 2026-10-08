package com.example.demo.service;

import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.EnfantRepository;
import com.example.demo.repository.MouvementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Solde restant d'un parent = total des cours suivis par ses enfants - total deja paye.
 * Recalcule apres chaque inscription, desinscription ou paiement.
 */
@Service
@Transactional
public class SoldeService {

    private final EnfantRepository enfantRepository;
    private final MouvementRepository mouvementRepository;

    public SoldeService(EnfantRepository enfantRepository, MouvementRepository mouvementRepository) {
        this.enfantRepository = enfantRepository;
        this.mouvementRepository = mouvementRepository;
    }

    public void recalculer(Utilisateur u) {
        enfantRepository.flush();
        mouvementRepository.flush();
        BigDecimal du = enfantRepository.totalDu(u.getId());
        BigDecimal paye = mouvementRepository.totalPaye(u.getId());
        u.setSoldeRestant(du.subtract(paye));
    }
}
