package com.example.b2_taches.controller;

import com.example.b2_taches.entity.Tache;
import com.example.b2_taches.repository.TacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TacheController {

    private final TacheRepository tacheRepository;

    @GetMapping("/taches")
    public List<Tache> taches(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int parPage
    ) {
        return tacheRepository.findAll(page, parPage);
    }

    @GetMapping("/taches/{id}")
    public Tache uneTache(@PathVariable Long id) {
        return tacheRepository.findById(id).get();
    }
}