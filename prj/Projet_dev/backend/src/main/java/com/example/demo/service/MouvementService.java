package com.example.demo.service;

import com.example.demo.dto.MouvementRequest;
import com.example.demo.dto.MouvementResponse;
import com.example.demo.entity.Mouvement;
import com.example.demo.entity.Utilisateur;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.MouvementRepository;
import com.example.demo.repository.UtilisateurRepository;
import com.example.demo.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MouvementService {

    private final MouvementRepository mouvementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final SoldeService soldeService;

    public MouvementService(MouvementRepository mouvementRepository, UtilisateurRepository utilisateurRepository,
                            SoldeService soldeService) {
        this.mouvementRepository = mouvementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.soldeService = soldeService;
    }

    // Admin : tous les paiements (ou ceux d'un utilisateur). Parent : les siens.
    @Transactional(readOnly = true)
    public List<MouvementResponse> findAll(CurrentUser user, Long utilisateurId) {
        List<Mouvement> liste;
        if (!user.admin()) {
            liste = mouvementRepository.findByUtilisateurIdOrderByDateDescIdDesc(user.id());
        } else if (utilisateurId != null) {
            liste = mouvementRepository.findByUtilisateurIdOrderByDateDescIdDesc(utilisateurId);
        } else {
            liste = mouvementRepository.findAllByOrderByDateDescIdDesc();
        }
        return liste.stream().map(MouvementResponse::from).toList();
    }

    public MouvementResponse create(MouvementRequest request, CurrentUser user) {
        Long cibleId = user.admin() ? request.utilisateurId() : user.id();
        if (cibleId == null) {
            throw ApiException.badRequest("Choisissez le parent concerné par ce paiement");
        }
        Utilisateur u = utilisateurRepository.findById(cibleId)
                .orElseThrow(() -> ApiException.notFound("Utilisateur introuvable : " + cibleId));

        Mouvement m = new Mouvement();
        m.setUtilisateur(u);
        m.setMontant(request.montant());
        m.setMoyenPaiement(request.moyenPaiement());
        m.setDate(user.admin() && request.date() != null ? request.date() : LocalDate.now());
        u.getMouvements().add(m);
        mouvementRepository.save(m);

        soldeService.recalculer(u);
        return MouvementResponse.from(m);
    }

    public void delete(Long id) {
        Mouvement m = mouvementRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Paiement introuvable : " + id));
        Utilisateur u = m.getUtilisateur();
        u.getMouvements().remove(m);
        mouvementRepository.delete(m);
        soldeService.recalculer(u);
    }
}
