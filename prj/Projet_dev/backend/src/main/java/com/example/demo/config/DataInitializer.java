package com.example.demo.config;

import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Cree un compte ADMIN au demarrage s'il n'existe pas encore.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public DataInitializer(UtilisateurRepository utilisateurRepository,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.admin.email}") String adminEmail,
                           @Value("${app.admin.password}") String adminPassword) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (utilisateurRepository.existsByEmail(adminEmail)) {
            return;
        }
        Utilisateur admin = new Utilisateur();
        admin.setNom("Admin");
        admin.setPrenom("Super");
        admin.setEmail(adminEmail);
        admin.setMotDePasse(passwordEncoder.encode(adminPassword));
        admin.setRole("ADMIN");
        admin.setSoldeRestant(BigDecimal.ZERO);
        utilisateurRepository.save(admin);
        log.info("Compte admin cree : {} / {}", adminEmail, adminPassword);
    }
}
