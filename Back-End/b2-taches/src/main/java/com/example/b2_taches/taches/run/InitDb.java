package com.example.b2_taches.taches.run;

import com.example.b2_taches.entity.Tache;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.b2_taches.repository.TacheRepository;

@Component
@RequiredArgsConstructor
public class InitDb implements CommandLineRunner {

    //-------
    // Champs

    private final TacheRepository repo;

    //-------
    // run()

    @Override
    public void run(String... args) throws Exception {
        repo.save( new Tache( null, "Promener le chien", true ) );
        repo.save( new Tache( null, "Créer un blog sur un sujet qui me passionne", false ) );
        repo.save( new Tache( null, "Découvrir un nouveau genre musical", true ) );
        repo.save( new Tache( null, "Apprendre la sténographie", true ) );
        repo.save( new Tache( null, "Pratiquer la programmation dans mon langage préféré", false ) );
        repo.save( new Tache( null, "Lire un article de recherche scientifique sur un sujet intéressant", false ) );
        repo.save( new Tache( null, "Apprendre à siffler avec les doigts", false ) );
        repo.save( new Tache( null, "Construire une cabane avec des coussins", false ) );
        repo.save( new Tache( null, "Faire une sieste après avoir bu un café", false ) );
        repo.save( new Tache( null, "Trouver un projet de bricolage à réaliser", false ) );
        repo.save( new Tache( null, "Aller à un concert avec des amis", false ) );
        repo.save( new Tache( null, "Consacrer dix minutes d'attention à son animal de compagnie", true ) );
        repo.save( new Tache( null, "Réorganiser et ranger la chambre", false ) );
        repo.save( new Tache( null, "Apprendre un nouveau sport", false ) );
        repo.save( new Tache( null, "Préparer une glace maison", true ) );
        repo.save( new Tache( null, "Organiser un vide-greniers", true ) );
        repo.save( new Tache( null, "Apprendre à jouer d'un nouvel instrument", false ) );
        repo.save( new Tache( null, "Résoudre un problème que je repousse depuis longtemps", false ) );
        repo.save( new Tache( null, "Préparer une recette que je n'ai jamais essayée", true ) );
        repo.save( new Tache( null, "Ranger la commode", true ) );
        repo.save( new Tache( null, "Réparer quelque chose de cassé dans la maison", true ) );
        repo.save( new Tache( null, "Jouer à un jeu vidéo", false ) );
        repo.save( new Tache( null, "Écouter de la musique que je n'ai pas entendue depuis longtemps", false ) );
        repo.save( new Tache( null, "Examiner mes finances et trouver une façon d'économiser de l'argent", false ) );
        repo.save( new Tache( null, "Apprendre le tableau périodique des éléments", true ) );
        repo.save( new Tache( null, "Établir un budget", true ) );
        repo.save( new Tache( null, "Donner son sang dans un centre de collecte", false ) );
        repo.save( new Tache( null, "Aller à la bibliothèque et trouver un livre intéressant", false ) );
        repo.save( new Tache( null, "Configurer l'authentification à deux facteurs sur mes comptes", false ) );
        repo.save( new Tache( null, "Faire une sieste", true ) );
        repo.save( new Tache( null, "Mettre à jour mon CV", true ) );
        repo.save( new Tache( null, "Faire une séance d'improvisation musicale avec des amis", true ) );
        repo.save( new Tache( null, "Organiser un tournoi de jeux vidéo entre amis", true ) );
        repo.save( new Tache( null, "Se tenir informé de l'actualité mondiale", false ) );

    }
}
