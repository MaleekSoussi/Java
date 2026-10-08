package com.example.demo.service;

import com.example.demo.dto.UtilisateurRequest;
import com.example.demo.dto.UtilisateurResponse;
import com.example.demo.entity.Cours;
import com.example.demo.entity.Enfant;
import com.example.demo.entity.Utilisateur;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UtilisateurResponse> findAll(String role) {
        List<Utilisateur> liste = (role == null || role.isBlank())
                ? utilisateurRepository.findAllByOrderByNomAsc()
                : utilisateurRepository.findByRoleOrderByNomAsc(role.toUpperCase());
        return liste.stream().map(UtilisateurResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UtilisateurResponse findById(Long id) {
        return UtilisateurResponse.from(get(id));
    }

    public UtilisateurResponse create(UtilisateurRequest request) {
        String email = request.email().trim().toLowerCase();
        if (utilisateurRepository.existsByEmail(email)) {
            throw ApiException.badRequest("Cet email est déjà utilisé");
        }
        if (request.motDePasse() == null || request.motDePasse().isBlank()) {
            throw ApiException.badRequest("Le mot de passe est obligatoire");
        }
        Utilisateur u = new Utilisateur();
        remplir(u, request, email);
        u.setMotDePasse(passwordEncoder.encode(request.motDePasse()));
        u.setSoldeRestant(BigDecimal.ZERO);
        return UtilisateurResponse.from(utilisateurRepository.save(u));
    }

    public UtilisateurResponse update(Long id, UtilisateurRequest request, Long currentUserId) {
        Utilisateur u = get(id);
        String email = request.email().trim().toLowerCase();
        if (!u.getEmail().equals(email) && utilisateurRepository.existsByEmail(email)) {
            throw ApiException.badRequest("Cet email est déjà utilisé");
        }
        if (u.getId().equals(currentUserId) && !"ADMIN".equals(request.role())) {
            throw ApiException.badRequest("Vous ne pouvez pas retirer votre propre rôle admin");
        }
        remplir(u, request, email);
        if (request.motDePasse() != null && !request.motDePasse().isBlank()) {
            u.setMotDePasse(passwordEncoder.encode(request.motDePasse()));
        }
        return UtilisateurResponse.from(u);
    }

    public void delete(Long id, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw ApiException.badRequest("Vous ne pouvez pas supprimer votre propre compte");
        }
        Utilisateur u = get(id);
        // Rendre les places des cours suivis par ses enfants
        for (Enfant e : u.getEnfants()) {
            for (Cours c : e.getCours()) {
                c.setPlacesDisponibles(c.getPlacesDisponibles() + 1);
                c.getEnfants().remove(e);
            }
            e.getCours().clear();
        }
        utilisateurRepository.delete(u);   // enfants et paiements supprimes en cascade
    }

    private void remplir(Utilisateur u, UtilisateurRequest r, String email) {
        u.setNom(r.nom().trim());
        u.setPrenom(r.prenom().trim());
        u.setEmail(email);
        u.setRole(r.role() != null ? r.role() : "PARENT");
        u.setModePaiement(r.modePaiement());
        u.setNombrePaiements(r.nombrePaiements());
    }

    private Utilisateur get(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Utilisateur introuvable : " + id));
    }
}
