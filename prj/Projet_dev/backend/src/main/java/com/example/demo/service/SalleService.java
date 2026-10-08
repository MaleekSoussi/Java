package com.example.demo.service;

import com.example.demo.dto.SalleRequest;
import com.example.demo.dto.SalleResponse;
import com.example.demo.entity.Salle;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CoursRepository;
import com.example.demo.repository.SalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SalleService {

    private final SalleRepository salleRepository;
    private final CoursRepository coursRepository;

    public SalleService(SalleRepository salleRepository, CoursRepository coursRepository) {
        this.salleRepository = salleRepository;
        this.coursRepository = coursRepository;
    }

    @Transactional(readOnly = true)
    public List<SalleResponse> findAll() {
        return salleRepository.findAllByOrderByNomAsc().stream().map(SalleResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SalleResponse findById(Long id) {
        return SalleResponse.from(get(id));
    }

    public SalleResponse create(SalleRequest request) {
        if (salleRepository.existsByNomIgnoreCase(request.nom().trim())) {
            throw ApiException.badRequest("Une salle porte déjà ce nom");
        }
        Salle s = new Salle();
        s.setNom(request.nom().trim());
        s.setCapacite(request.capacite());
        return SalleResponse.from(salleRepository.save(s));
    }

    public SalleResponse update(Long id, SalleRequest request) {
        Salle s = get(id);
        String nom = request.nom().trim();
        if (!s.getNom().equalsIgnoreCase(nom) && salleRepository.existsByNomIgnoreCase(nom)) {
            throw ApiException.badRequest("Une salle porte déjà ce nom");
        }
        s.setNom(nom);
        s.setCapacite(request.capacite());
        return SalleResponse.from(s);
    }

    public void delete(Long id) {
        Salle s = get(id);
        coursRepository.findBySalleId(id).forEach(c -> c.setSalle(null));
        salleRepository.delete(s);
    }

    private Salle get(Long id) {
        return salleRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Salle introuvable : " + id));
    }
}
