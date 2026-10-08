package com.example.b2_taches.controller;

import com.example.b2_taches.entity.Tache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello !";
    }

    @GetMapping("/todo")
    public Tache todo() {
        return new Tache(1L, "Atelier B2", false);
    }
}