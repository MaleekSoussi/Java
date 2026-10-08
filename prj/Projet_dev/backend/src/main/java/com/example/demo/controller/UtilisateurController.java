package com.example.demo.controller;

import com.example.demo.dto.UtilisateurRequest;
import com.example.demo.dto.UtilisateurResponse;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Reserve a l'ADMIN (voir SecurityConfig)
@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // GET /api/utilisateurs?role=PARENT pour filtrer
    @GetMapping
    public List<UtilisateurResponse> getAll(@RequestParam(required = false) String role) {
        return utilisateurService.findAll(role);
    }

    @GetMapping("/{id}")
    public UtilisateurResponse getById(@PathVariable Long id) {
        return utilisateurService.findById(id);
    }

    @PostMapping
    public ResponseEntity<UtilisateurResponse> create(@Valid @RequestBody UtilisateurRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurService.create(request));
    }

    @PutMapping("/{id}")
    public UtilisateurResponse update(@PathVariable Long id, @Valid @RequestBody UtilisateurRequest request,
                                      @AuthenticationPrincipal Jwt jwt) {
        return utilisateurService.update(id, request, CurrentUser.from(jwt).id());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        utilisateurService.delete(id, CurrentUser.from(jwt).id());
        return ResponseEntity.noContent().build();
    }
}
