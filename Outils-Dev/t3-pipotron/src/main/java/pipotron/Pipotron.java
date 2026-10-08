package pipotron;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Pipotron
 * Générateur de phrases aléatoires
 */
public class Pipotron {

    private static final int NB_LISTES = 6;
    private final List<List<String>> listes = new ArrayList<>(NB_LISTES);
    private final Random rnd = new Random();

    /** Constructeur  */
    public Pipotron() throws IOException {
        loadAllLists();
    }

    /** Retourne la liste des listes  */
    public List<List<String>> getListes() {
        return listes;
    }

    /**
     * Charge les six fichiers texte dans la collection {@code listes}.
     */
    private void loadAllLists() throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        for (int i = 0; i < NB_LISTES; i++) {
            String fileName = "liste" + i + ".txt";
            try (InputStream is = cl.getResourceAsStream(fileName)) {
                if (is == null) {
                    throw new IOException("Ressource introuvable : " + fileName);
                }
                listes.add(readLines(is));
            }
        }
    }

    /**
     * Lit toutes les lignes d’un flux et les retourne sous forme de liste.
     */
    private List<String> readLines(InputStream is) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    /**
     * Génère une phrase en prenant un fragment aléatoire dans chaque liste.
     */
    public String generate() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NB_LISTES; i++) {
            List<String> list = listes.get(i);
            if (!list.isEmpty()) {
                sb.append(randomElement(list)).append(' ');
            }
        }
        // On enlève le dernier espace et on ajoute le point.
        if (!sb.isEmpty()) {
            sb.setLength(sb.length() - 1);
            sb.append('.');
        }
        return sb.toString();
    }

    private String randomElement(List<String> list) {
        return list.get(rnd.nextInt(list.size()));
    }

    //-------
    // main()
    //-------
    public static void main(String[] args) {
        try {
            Pipotron pipotron = new Pipotron();

            // Affiche une phrase aléatoire
            System.out.println("------------------------------------------");
            System.out.println(pipotron.generate());
            System.out.println("------------------------------------------");
            // Affiche les 25 mots les plus utilisés
            Analyseur.top(25, pipotron.getListes());

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des listes : " + e.getMessage());
        }
    }

}