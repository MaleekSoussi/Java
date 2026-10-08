package com.example.b2_taches.repository;

import com.example.b2_taches.entity.Tache;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TacheRepository {

    //-------
    // Champs

    private final Map<Long, Tache> taches = new HashMap<>();
    private long currentId = 0;

    //-------
    // Comparateur

    private final Comparator<Tache> idComparator = Comparator
                .comparing(Tache::getId);

    //-------
    // Actons

    // Retourne toutes les taches
    public List<Tache> findAll() {
        val liste = new ArrayList<>( taches.values() );
        liste.sort(idComparator);
        return liste;
    }

    // Retourne les taches d'une page
    public List<Tache> findAll( int page, int parPage ) {
        val liste = new ArrayList<>( taches.values() );
        liste.sort(idComparator);
        val skip = Math.clamp((long) (page - 1) * parPage, 0, liste.size() );
        val last = Math.clamp((long) page * parPage, 0, liste.size() );
        return liste.subList( skip, last );
    }

    // Trouve une tâche par son id
    public Optional<Tache> findById(Long id) {
        Tache tache = null;
        if (taches.containsKey(id)) tache = taches.get(id);
        return Optional.ofNullable(tache);
    }

    //Vérifie qu'une tâche existe
    public boolean existsById(Long id) {
        return taches.containsKey(id);
    }

    // Enregistre la mise à jour (ajout ou modification)
    public Tache save(Tache tache) {
       Tache tacheAModifier = null;
        if( tache.getId() != null ) {
            tacheAModifier = taches.get(tache.getId());
        }
        if( tacheAModifier == null ) {
            tache.setId(++currentId);
        }
        taches.put(tache.getId(), tache);
        return tache;
    }

    // Supprime la tâche ayant l' id
    public Optional<Tache> deleteById(Long id) {
        Tache currentTache = taches.get(id);

        if (currentTache != null) {
            taches.remove(id);
        }
        return Optional.ofNullable(currentTache);
    }    

}

