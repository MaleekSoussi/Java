package com.example.demo.controller;

import com.example.demo.dto.NiveauRequest;
import com.example.demo.dto.NiveauResponse;
import com.example.demo.service.NiveauService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/niveaux")
public class NiveauController {

    private final NiveauService niveauService;

    public NiveauController(NiveauService niveauService) {
        this.niveauService = niveauService;
    }

    @GetMapping
    public List<NiveauResponse> getAll() {
        return niveauService.findAll();
    }

    @GetMapping("/{id}")
    public NiveauResponse getById(@PathVariable Long id) {
        return niveauService.findById(id);
    }

    @PostMapping
    public ResponseEntity<NiveauResponse> create(@Valid @RequestBody NiveauRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(niveauService.create(request));
    }

    @PutMapping("/{id}")
    public NiveauResponse update(@PathVariable Long id, @Valid @RequestBody NiveauRequest request) {
        return niveauService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        niveauService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
