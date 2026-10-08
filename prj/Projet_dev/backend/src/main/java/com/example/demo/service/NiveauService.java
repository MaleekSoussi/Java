package com.example.demo.service;

import com.example.demo.dto.NiveauRequest;
import com.example.demo.dto.NiveauResponse;
import com.example.demo.entity.Niveau;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CoursRepository;
import com.example.demo.repository.EnfantRepository;
import com.example.demo.repository.NiveauRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NiveauService {

    private final NiveauRepository niveauRepository;
    private final CoursRepository coursRepository;
    private final EnfantRepository enfantRepository;

    public NiveauService(NiveauRepository niveauRepository, CoursRepository coursRepository,
                         EnfantRepository enfantRepository) {
        this.niveauRepository = niveauRepository;
        this.coursRepository = coursRepository;
        this.enfantRepository = enfantRepository;
    }

    @Transactional(readOnly = true)
    public List<NiveauResponse> findAll() {
        return niveauRepository.findAllByOrderByLibelleAsc().stream().map(NiveauResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public NiveauResponse findById(Long id) {
        return NiveauResponse.from(get(id));
    }

    public NiveauResponse create(NiveauRequest request) {
        if (niveauRepository.existsByLibelleIgnoreCase(request.libelle().trim())) {
            throw ApiException.badRequest("Ce niveau existe déjà");
        }
        Niveau n = new Niveau();
        n.setLibelle(request.libelle().trim());
        n.setMontant(request.montant());
        return NiveauResponse.from(niveauRepository.save(n));
    }

    public NiveauResponse update(Long id, NiveauRequest request) {
        Niveau n = get(id);
        String libelle = request.libelle().trim();
        if (!n.getLibelle().equalsIgnoreCase(libelle) && niveauRepository.existsByLibelleIgnoreCase(libelle)) {
            throw ApiException.badRequest("Ce niveau existe déjà");
        }
        n.setLibelle(libelle);
        n.setMontant(request.montant());
        return NiveauResponse.from(n);
    }

    public void delete(Long id) {
        Niveau n = get(id);
        // Detacher les cours et enfants de ce niveau avant de le supprimer
        coursRepository.findByNiveauId(id).forEach(c -> c.setNiveau(null));
        enfantRepository.findByNiveauId(id).forEach(e -> e.setNiveau(null));
        niveauRepository.delete(n);
    }

    private Niveau get(Long id) {
        return niveauRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Niveau introuvable : " + id));
    }
}
