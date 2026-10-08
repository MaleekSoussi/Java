package pipotron;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    /** Charge le fichier JSON dans l'objet listes */
    public static void load( List<List<String>> listes ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        // Lecture du fichier depuis le classpath (et non depuis le disque)
        try (InputStream flux = JsonUtil.class.getResourceAsStream("/fragments.json")) {
            if (flux == null) {
                throw new IOException("Fichier fragments.json introuvable sur le classpath");
            }

            Map<String, List<String>> donnees = mapper.readValue(
                    flux,
                    new TypeReference<LinkedHashMap<String, List<String>>>() {}
            );

            listes.addAll( donnees.values() );
        }

    }

    /** Crée le fichier JSON à partir de l'objet listes */
    static void write(List<List<String>> donnees) throws IOException {
        Map<String, List<String>> objet = new LinkedHashMap<>();
        for (int i = 0; i < donnees.size(); i++) {
            objet.put("liste" + i, donnees.get(i));
        }
        ObjectMapper mapper = new ObjectMapper();
        final String cheminFichier = "src/main/resources/fragments.json";
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(cheminFichier), objet);
    }


}