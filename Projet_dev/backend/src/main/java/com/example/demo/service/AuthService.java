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

    // Inscription
    public AuthResponse signup(SignupRequest request) {
        if (utilisateurRepository.existsByEmail(request.email())) {
            throw ApiException.badRequest("Cet email est déjà utilisé");
        }

        Utilisateur u = new Utilisateur();
        u.setNom(request.nom());
        u.setPrenom(request.prenom());
        u.setEmail(request.email());
        u.setMotDePasse(passwordEncoder.encode(request.motDePasse()));  // mot de passe haché
        u.setRole("PARENT");                                             // toujours PARENT à l'inscription
        u.setModePaiement(request.modePaiement());
        u.setNombrePaiements(request.nombrePaiements());
        u.setSoldeRestant(BigDecimal.ZERO);

        Utilisateur saved = utilisateurRepository.save(u);
        return buildResponse(saved);
    }

    // Connexion
    @Transactional(readOnly = true)
    public AuthResponse signin(LoginRequest request) {
        Utilisateur u = utilisateurRepository.findByEmail(request.email())
                .orElseThrow(() -> ApiException.unauthorized("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.motDePasse(), u.getMotDePasse())) {
            throw ApiException.unauthorized("Email ou mot de passe incorrect");
        }
        return buildResponse(u);
    }

    // Utilisateur connecté (à partir de l'email contenu dans le token)
    @Transactional(readOnly = true)
    public UtilisateurResponse me(String email) {
        return utilisateurRepository.findByEmail(email)
                .map(UtilisateurResponse::from)
                .orElseThrow(() -> ApiException.notFound("Utilisateur introuvable"));
    }

    private AuthResponse buildResponse(Utilisateur u) {
        return new AuthResponse(
                jwtService.generateToken(u),
                "Bearer",
                jwtService.getExpirationSeconds(),
                UtilisateurResponse.from(u)
        );
    }
}