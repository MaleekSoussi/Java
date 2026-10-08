package com.example.demo.controller;

import com.example.demo.dto.MouvementRequest;
import com.example.demo.dto.MouvementResponse;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.MouvementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Paiements. Parent : les siens. Admin : tous (suppression reservee a l'admin).
@RestController
@RequestMapping("/api/mouvements")
public class MouvementController {

    private final MouvementService mouvementService;

    public MouvementController(MouvementService mouvementService) {
        this.mouvementService = mouvementService;
    }

    @GetMapping
    public List<MouvementResponse> getAll(@RequestParam(required = false) Long utilisateurId,
                                          @AuthenticationPrincipal Jwt jwt) {
        return mouvementService.findAll(CurrentUser.from(jwt), utilisateurId);
    }

    @PostMapping
    public ResponseEntity<MouvementResponse> create(@Valid @RequestBody MouvementRequest request,
                                                    @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mouvementService.create(request, CurrentUser.from(jwt)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mouvementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
