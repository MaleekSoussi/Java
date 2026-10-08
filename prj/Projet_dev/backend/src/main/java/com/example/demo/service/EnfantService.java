package com.example.demo.service;

import com.example.demo.dto.EnfantRequest;
import com.example.demo.dto.EnfantResponse;
import com.example.demo.entity.Cours;
import com.example.demo.entity.Enfant;
import com.example.demo.entity.Niveau;
import com.example.demo.entity.Utilisateur;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CoursRepository;
import com.example.demo.repository.EnfantRepository;
import com.example.demo.repository.NiveauRepository;
import com.example.demo.repository.UtilisateurRepository;
import com.example.demo.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class EnfantService {

    private final EnfantRepository enfantRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NiveauRepository niveauRepository;
    private final CoursRepository coursRepository;
    private final SoldeService soldeService;

    public EnfantService(EnfantRepository enfantRepository, UtilisateurRepository utilisateurRepository,
                         NiveauRepository niveauRepository, CoursRepository coursRepository,
                         SoldeService soldeService) {
        this.enfantRepository = enfantRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.niveauRepository = niveauRepository;
        this.coursRepository = coursRepository;
        this.soldeService = soldeService;
    }

    // Admin : tous les enfants. Parent : seulement les siens.
    @Transactional(readOnly = true)
    public List<EnfantResponse> findAll(CurrentUser user) {
        List<Enfant> liste = user.admin()
                ? enfantRepository.findAllByOrderByNomAscPrenomAsc()
                : enfantRepository.findByUtilisateurIdOrderByPrenomAsc(user.id());
        return liste.stream().map(EnfantResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EnfantResponse findById(Long id, CurrentUser user) {
        return EnfantResponse.from(getAutorise(id, user));
    }

    public EnfantResponse create(EnfantRequest request, CurrentUser user) {
        Long parentId = user.admin() ? request.utilisateurId() : user.id();
        if (parentId == null) {
            throw ApiException.badRequest("Choisissez le parent de l'enfant");
        }
        Utilisateur parent = utilisateurRepository.findById(parentId)
                .orElseThrow(() -> ApiException.notFound("Parent introuvable : " + parentId));

        Enfant e = new Enfant();
        remplir(e, request);
        e.setUtilisateur(parent);
        parent.getEnfants().add(e);
        return EnfantResponse.from(enfantRepository.save(e));
    }

    public EnfantResponse update(Long id, EnfantRequest request, CurrentUser user) {
        Enfant e = getAutorise(id, user);
        Long ancienNiveau = e.getNiveau() != null ? e.getNiveau().getId() : null;
        remplir(e, request);

        // Changement de niveau : l'enfant est desinscrit des cours qui ne sont plus de son niveau
        if (!Objects.equals(ancienNiveau, request.niveauId()) && request.niveauId() != null) {
            for (Cours c : new ArrayList<>(e.getCours())) {
                if (c.getNiveau() != null && !c.getNiveau().getId().equals(request.niveauId())) {
                    retirer(e, c);
                }
            }
            soldeService.recalculer(e.getUtilisateur());
        }

        // L'admin peut changer le parent
        if (user.admin() && request.utilisateurId() != null
                && !request.utilisateurId().equals(e.getUtilisateur().getId())) {
            Utilisateur ancien = e.getUtilisateur();
            Utilisateur nouveau = utilisateurRepository.findById(request.utilisateurId())
                    .orElseThrow(() -> ApiException.notFound("Parent introuvable : " + request.utilisateurId()));
            ancien.getEnfants().remove(e);
            e.setUtilisateur(nouveau);
            nouveau.getEnfants().add(e);
            soldeService.recalculer(ancien);
            soldeService.recalculer(nouveau);
        }
        return EnfantResponse.from(e);
    }

    public void delete(Long id, CurrentUser user) {
        Enfant e = getAutorise(id, user);
        Utilisateur parent = e.getUtilisateur();
        for (Cours c : new ArrayList<>(e.getCours())) {
            retirer(e, c);
        }
        parent.getEnfants().remove(e);
        enfantRepository.delete(e);
        soldeService.recalculer(parent);
    }

    // ---------- Inscriptions ----------

    public EnfantResponse inscrire(Long enfantId, Long coursId, CurrentUser user) {
        Enfant e = getAutorise(enfantId, user);
        Cours c = coursRepository.findById(coursId)
                .orElseThrow(() -> ApiException.notFound("Cours introuvable : " + coursId));

        if (e.getCours().contains(c)) {
            throw ApiException.badRequest(e.getPrenom() + " est déjà inscrit(e) à ce cours");
        }
        if (c.getPlacesDisponibles() == null || c.getPlacesDisponibles() <= 0) {
            throw ApiException.badRequest("Ce cours est complet");
        }
        if (e.getNiveau() != null && c.getNiveau() != null && !e.getNiveau().getId().equals(c.getNiveau().getId())) {
            throw ApiException.badRequest("Ce cours n'est pas du niveau de " + e.getPrenom());
        }
        boolean chevauchement = e.getCours().stream().anyMatch(autre ->
                autre.getJour().equals(c.getJour())
                        && autre.getHeureDebut().isBefore(c.getHeureFin())
                        && autre.getHeureFin().isAfter(c.getHeureDebut()));
        if (chevauchement) {
            throw ApiException.badRequest(e.getPrenom() + " a déjà un cours sur ce créneau");
        }

        e.getCours().add(c);
        c.getEnfants().add(e);
        c.setPlacesDisponibles(c.getPlacesDisponibles() - 1);
        soldeService.recalculer(e.getUtilisateur());
        return EnfantResponse.from(e);
    }

    public EnfantResponse desinscrire(Long enfantId, Long coursId, CurrentUser user) {
        Enfant e = getAutorise(enfantId, user);
        Cours c = e.getCours().stream().filter(x -> x.getId().equals(coursId)).findFirst()
                .orElseThrow(() -> ApiException.badRequest(e.getPrenom() + " n'est pas inscrit(e) à ce cours"));
        retirer(e, c);
        soldeService.recalculer(e.getUtilisateur());
        return EnfantResponse.from(e);
    }

    // ---------- Outils ----------

    private void retirer(Enfant e, Cours c) {
        e.getCours().remove(c);
        c.getEnfants().remove(e);
        c.setPlacesDisponibles((c.getPlacesDisponibles() == null ? 0 : c.getPlacesDisponibles()) + 1);
    }

    private void remplir(Enfant e, EnfantRequest r) {
        Niveau niveau = null;
        if (r.niveauId() != null) {
            niveau = niveauRepository.findById(r.niveauId())
                    .orElseThrow(() -> ApiException.notFound("Niveau introuvable : " + r.niveauId()));
        }
        e.setNom(r.nom().trim());
        e.setPrenom(r.prenom().trim());
        e.setDateNaissance(r.dateNaissance());
        e.setEtablissement(r.etablissement());
        e.setNiveau(niveau);
    }

    // Un parent ne peut acceder qu'a ses propres enfants
    private Enfant getAutorise(Long id, CurrentUser user) {
        Enfant e = enfantRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Enfant introuvable : " + id));
        if (!user.admin() && !e.getUtilisateur().getId().equals(user.id())) {
            throw ApiException.forbidden("Cet enfant ne vous appartient pas");
        }
        return e;
    }
}
