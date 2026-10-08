package com.example.demo.controller;

import com.example.demo.dto.SalleRequest;
import com.example.demo.dto.SalleResponse;
import com.example.demo.service.SalleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salles")
public class SalleController {

    private final SalleService salleService;

    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }

    @GetMapping
    public List<SalleResponse> getAll() {
        return salleService.findAll();
    }

    @GetMapping("/{id}")
    public SalleResponse getById(@PathVariable Long id) {
        return salleService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SalleResponse> create(@Valid @RequestBody SalleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salleService.create(request));
    }

    @PutMapping("/{id}")
    public SalleResponse update(@PathVariable Long id, @Valid @RequestBody SalleRequest request) {
        return salleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
