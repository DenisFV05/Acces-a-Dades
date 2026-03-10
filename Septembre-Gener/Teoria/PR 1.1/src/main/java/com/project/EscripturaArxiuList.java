package com.project;

import com.project.utilitats.UtilitatsFitxers;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class EscripturaArxiuList {

    // Mètode per escriure en un arxiu de text
    public static void escriureArxiu(List<String> linies, String camiFitxer) throws IOException {
        // Escriure en l'arxiu
        Path sortida = Paths.get(camiFitxer);
        Files.write(sortida, linies, Charset.defaultCharset());
    }

    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "ArxiuList.txt";

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Crear llista de línies per escriure
            List<String> linies = new ArrayList<>();
            linies.add("Del xoc i la confusió apareixen les pors,");
            linies.add("perills i destruccions inapreciables per la");
            linies.add("majoria de la gent, per sectors específics");
            linies.add("de la societat i la majoria de governants.");
            linies.add("La natura, a través d'huracans, terratrèmols,");
            linies.add("fam i pandèmies genera xoc i confusió.");

            // Utilitzar el mètode escriureArxiu per escriure el contingut al fitxer
            escriureArxiu(linies, camiFitxer);

            System.out.println("Llest");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
