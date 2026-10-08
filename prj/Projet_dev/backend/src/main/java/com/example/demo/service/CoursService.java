package com.example.demo.service;

import com.example.demo.dto.CoursRequest;
import com.example.demo.dto.CoursResponse;
import com.example.demo.dto.EnfantResponse;
import com.example.demo.entity.Cours;
import com.example.demo.entity.Enfant;
import com.example.demo.entity.Niveau;
import com.example.demo.entity.Salle;
import com.example.demo.entity.Utilisateur;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CoursRepository;
import com.example.demo.repository.EnfantRepository;
import com.example.demo.repository.NiveauRepository;
import com.example.demo.repository.SalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CoursService {

    // Ordre d'affichage des jours
    private static final List<String> JOURS =
            List.of("LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "DIMANCHE");

    private final CoursRepository coursRepository;
    private final SalleRepository salleRepository;
    private final NiveauRepository niveauRepository;
    private final EnfantRepository enfantRepository;
    private final SoldeService soldeService;

    public CoursService(CoursRepository coursRepository, SalleRepository salleRepository,
                        NiveauRepository niveauRepository, EnfantRepository enfantRepository,
                        SoldeService soldeService) {
        this.coursRepository = coursRepository;
        this.salleRepository = salleRepository;
        this.niveauRepository = niveauRepository;
        this.enfantRepository = enfantRepository;
        this.soldeService = soldeService;
    }

    @Transactional(readOnly = true)
    public List<CoursResponse> findAll(Long niveauId) {
        List<Cours> liste = niveauId != null ? coursRepository.findByNiveauId(niveauId) : coursRepository.findAll();
        return liste.stream()
                .sorted(Comparator.comparing((Cours c) -> JOURS.indexOf(c.getJour()))
                        .thenComparing(Cours::getHeureDebut))
                .map(CoursResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CoursResponse findById(Long id) {
        return CoursResponse.from(get(id));
    }

    @Transactional(readOnly = true)
    public List<EnfantResponse> inscrits(Long id) {
        get(id);
        return enfantRepository.findByCoursIdOrderByNomAsc(id).stream().map(EnfantResponse::from).toList();
    }

    public CoursResponse create(CoursRequest request) {
        Cours c = new Cours();
        remplir(c, request, null);
        return CoursResponse.from(coursRepository.save(c));
    }

    public CoursResponse update(Long id, CoursRequest request) {
        Cours c = get(id);
        Long ancienNiveau = c.getNiveau() != null ? c.getNiveau().getId() : null;
        remplir(c, request, id);
        // Le prix depend du niveau : si le niveau change, recalculer le solde des parents concernes
        if (!Objects.equals(ancienNiveau, request.niveauId())) {
            parentsDes(c.getEnfants()).forEach(soldeService::recalculer);
        }
        return CoursResponse.from(c);
    }

    public void delete(Long id) {
        Cours c = get(id);
        Set<Utilisateur> parents = parentsDes(c.getEnfants());
        for (Enfant e : new ArrayList<>(c.getEnfants())) {
            e.getCours().remove(c);
        }
        c.getEnfants().clear();
        coursRepository.delete(c);
        parents.forEach(soldeService::recalculer);
    }

    private void remplir(Cours c, CoursRequest r, Long coursId) {
        if (!r.heureFin().isAfter(r.heureDebut())) {
            throw ApiException.badRequest("L'heure de fin doit être après l'heure de début");
        }
        Salle salle = null;
        if (r.salleId() != null) {
            salle = salleRepository.findById(r.salleId())
                    .orElseThrow(() -> ApiException.notFound("Salle introuvable : " + r.salleId()));
            if (coursRepository.salleOccupee(salle.getId(), r.jour(), r.heureDebut(), r.heureFin(), coursId)) {
                throw ApiException.badRequest("La salle " + salle.getNom() + " est déjà occupée sur ce créneau");
            }
            int inscrits = c.getEnfants().size();
            if (salle.getCapacite() != null && r.placesDisponibles() + inscrits > salle.getCapacite()) {
                throw ApiException.badRequest("La salle " + salle.getNom() + " ne peut accueillir que "
                        + salle.getCapacite() + " enfants (" + inscrits + " déjà inscrits)");
            }
        }
        Niveau niveau = null;
        if (r.niveauId() != null) {
            niveau = niveauRepository.findById(r.niveauId())
                    .orElseThrow(() -> ApiException.notFound("Niveau introuvable : " + r.niveauId()));
        }
        c.setJour(r.jour());
        c.setHeureDebut(r.heureDebut());
        c.setHeureFin(r.heureFin());
        c.setPlacesDisponibles(r.placesDisponibles());
        c.setSalle(salle);
        c.setNiveau(niveau);
    }

    private Set<Utilisateur> parentsDes(Collection<Enfant> enfants) {
        Set<Utilisateur> parents = new HashSet<>();
        enfants.forEach(e -> parents.add(e.getUtilisateur()));
        return parents;
    }

    private Cours get(Long id) {
        return coursRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Cours introuvable : " + id));
    }
}
