package pipotron;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import java.util.List;

public class Analyseur {

    /** Affiche nes nb mots les plus utilisés dans l'ensemble des listes */
    public static void top(int nb, List<List<String>> listes) {

        // Comptage des occurrences de chaque mot
        Multiset<String> compteur = HashMultiset.create();

        for (List<String> liste : listes) {
            for (String chaine : liste) {
                for (String mot : chaine.split("\\s+")) {
                    if (!mot.isEmpty()) {
                        compteur.add(mot);
                    }
                }
            }
        }

        // Tri par occurrence décroissante
        Multiset<String> trie = Multisets.copyHighestCountFirst(compteur);

        // Affichage des nb mots les plus utilisés
        int rang = 1;
        for (Multiset.Entry<String> entree : trie.entrySet()) {
            if (rang > nb) break;
            System.out.printf("%2d. %-15s : %d occurrence(s)%n",
                    rang, entree.getElement(), entree.getCount());
            rang++;
        }
    }

}