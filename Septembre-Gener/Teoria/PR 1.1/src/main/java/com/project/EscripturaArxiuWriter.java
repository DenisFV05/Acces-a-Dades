package com.project;

import com.project.utilitats.UtilitatsFitxers;
import java.io.FileWriter;
import java.io.IOException;

public class EscripturaArxiuWriter {

    // Mètode per escriure en un fitxer
    public static void escriureFitxer(String camiFitxer, String contingut) throws IOException {
        try (FileWriter fw = new FileWriter(camiFitxer)) {
            fw.write(contingut);
        }
    }

    // Mètode per afegir text a un fitxer
    public static void afegirAlFitxer(String camiFitxer, String contingut) throws IOException {
        try (FileWriter fw = new FileWriter(camiFitxer, true)) {
            fw.write(contingut);
        }
    }

    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "ArxiuWriter.txt";

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Escriure contingut inicial al fitxer
            escriureFitxer(camiFitxer, "Sometimes life hits you in the head with a brick\n"
                    + "Don’t lose faith. I’m convinced that the only\n");

            // Afegir més contingut al fitxer
            afegirAlFitxer(camiFitxer, "thing that kept me going was that I loved what I did.\n"
                    + "You’ve got to find what you love.\n");

            System.out.println("Llest");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
