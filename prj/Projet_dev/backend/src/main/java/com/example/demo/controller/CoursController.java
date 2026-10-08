package com.example.demo.controller;

import com.example.demo.dto.CoursRequest;
import com.example.demo.dto.CoursResponse;
import com.example.demo.dto.EnfantResponse;
import com.example.demo.service.CoursService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
public class CoursController {

    private final CoursService coursService;

    public CoursController(CoursService coursService) {
        this.coursService = coursService;
    }

    // GET /api/cours?niveauId=1 pour filtrer par niveau
    @GetMapping
    public List<CoursResponse> getAll(@RequestParam(required = false) Long niveauId) {
        return coursService.findAll(niveauId);
    }

    @GetMapping("/{id}")
    public CoursResponse getById(@PathVariable Long id) {
        return coursService.findById(id);
    }

    // Enfants inscrits a un cours (admin)
    @GetMapping("/{id}/enfants")
    public List<EnfantResponse> inscrits(@PathVariable Long id) {
        return coursService.inscrits(id);
    }

    @PostMapping
    public ResponseEntity<CoursResponse> create(@Valid @RequestBody CoursRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coursService.create(request));
    }

    @PutMapping("/{id}")
    public CoursResponse update(@PathVariable Long id, @Valid @RequestBody CoursRequest request) {
        return coursService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coursService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
