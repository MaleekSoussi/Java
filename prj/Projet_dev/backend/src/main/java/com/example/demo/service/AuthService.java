package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.dto.UtilisateurResponse;
import com.example.demo.entity.Utilisateur;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse signup(SignupRequest request) {
        String email = request.email().trim().toLowerCase();
        if (utilisateurRepository.existsByEmail(email)) {
            throw ApiException.badRequest("Cet email est déjà utilisé");
        }
        Utilisateur u = new Utilisateur();
        u.setNom(request.nom().trim());
        u.setPrenom(request.prenom().trim());
        u.setEmail(email);
        u.setMotDePasse(passwordEncoder.encode(request.motDePasse()));
        u.setRole("PARENT");                       // toujours PARENT a l'inscription
        u.setModePaiement(request.modePaiement());
        u.setNombrePaiements(request.nombrePaiements());
        u.setSoldeRestant(BigDecimal.ZERO);
        return buildResponse(utilisateurRepository.save(u));
    }

    @Transactional(readOnly = true)
    public AuthResponse signin(LoginRequest request) {
        Utilisateur u = utilisateurRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> ApiException.unauthorized("Email ou mot de passe incorrect"));
        if (!passwordEncoder.matches(request.motDePasse(), u.getMotDePasse())) {
            throw ApiException.unauthorized("Email ou mot de passe incorrect");
        }
        return buildResponse(u);
    }

    @Transactional(readOnly = true)
    public UtilisateurResponse me(Long id) {
        return utilisateurRepository.findById(id)
                .map(UtilisateurResponse::from)
                .orElseThrow(() -> ApiException.notFound("Utilisateur introuvable"));
    }

    private AuthResponse buildResponse(Utilisateur u) {
        return new AuthResponse(jwtService.generateToken(u), "Bearer",
                jwtService.getExpirationSeconds(), UtilisateurResponse.from(u));
    }
}
