package com.example.demo.controller;

import com.example.demo.dto.EnfantRequest;
import com.example.demo.dto.EnfantResponse;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.EnfantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Parent : ses enfants uniquement. Admin : tous les enfants.
@RestController
@RequestMapping("/api/enfants")
public class EnfantController {

    private final EnfantService enfantService;

    public EnfantController(EnfantService enfantService) {
        this.enfantService = enfantService;
    }

    @GetMapping
    public List<EnfantResponse> getAll(@AuthenticationPrincipal Jwt jwt) {
        return enfantService.findAll(CurrentUser.from(jwt));
    }

    @GetMapping("/{id}")
    public EnfantResponse getById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return enfantService.findById(id, CurrentUser.from(jwt));
    }

    @PostMapping
    public ResponseEntity<EnfantResponse> create(@Valid @RequestBody EnfantRequest request,
                                                 @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enfantService.create(request, CurrentUser.from(jwt)));
    }

    @PutMapping("/{id}")
    public EnfantResponse update(@PathVariable Long id, @Valid @RequestBody EnfantRequest request,
                                 @AuthenticationPrincipal Jwt jwt) {
        return enfantService.update(id, request, CurrentUser.from(jwt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        enfantService.delete(id, CurrentUser.from(jwt));
        return ResponseEntity.noContent().build();
    }

    // Inscrire un enfant a un cours
    @PostMapping("/{id}/cours/{coursId}")
    public EnfantResponse inscrire(@PathVariable Long id, @PathVariable Long coursId,
                                   @AuthenticationPrincipal Jwt jwt) {
        return enfantService.inscrire(id, coursId, CurrentUser.from(jwt));
    }

    // Desinscrire un enfant d'un cours
    @DeleteMapping("/{id}/cours/{coursId}")
    public EnfantResponse desinscrire(@PathVariable Long id, @PathVariable Long coursId,
                                      @AuthenticationPrincipal Jwt jwt) {
        return enfantService.desinscrire(id, coursId, CurrentUser.from(jwt));
    }
}
