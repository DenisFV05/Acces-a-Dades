package com.project;

import com.project.utilitats.UtilitatsFitxers;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GestioArxius {

    // Mètode per comprovar si un arxiu existeix
    public static boolean comprovarExistenciaArxiu(String camiFitxer) {
        File fitxer = new File(camiFitxer);
        return fitxer.exists();
    }

    // Mètode per crear un arxiu nou
    public static boolean crearArxiu(String camiFitxer) throws IOException {
        File fitxer = new File(camiFitxer);
        return fitxer.createNewFile();
    }

    // Mètode per esborrar un arxiu
    public static boolean esborrarArxiu(String camiFitxer) {
        File fitxer = new File(camiFitxer);
        return fitxer.delete();
    }

    // Mètode per comprovar si un camí és un directori
    public static boolean esDirectori(String camiFitxer) {
        File fitxer = new File(camiFitxer);
        return fitxer.isDirectory();
    }

    // Mètode per comprovar si un camí és un arxiu
    public static boolean esArxiu(String camiFitxer) {
        File fitxer = new File(camiFitxer);
        return fitxer.isFile();
    }

    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "GestioArxius.txt";

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Comprovar que un arxiu existeix
            if (comprovarExistenciaArxiu(camiFitxer)) {
                System.out.println("L'arxiu \"" + camiFitxer + "\" existeix.");
            }

            // Crear un arxiu temporal
            String camiFitxerTmp = camiBase + "ArxiuTmp.txt";
            if (crearArxiu(camiFitxerTmp)) {
                System.out.println("S'ha creat l'arxiu temporal \"" + camiFitxerTmp + "\".");
            } else {
                System.out.println("No s'ha pogut crear l'arxiu temporal.");
            }

            // Esperar 1 segon
            TimeUnit.SECONDS.sleep(1);

            // Esborrar l'arxiu temporal
            if (esborrarArxiu(camiFitxerTmp)) {
                System.out.println("S'ha esborrat l'arxiu temporal \"" + camiFitxerTmp + "\".");
            } else {
                System.out.println("No s'ha pogut esborrar l'arxiu temporal.");
            }

            // Comprovar si un camí és un directori
            if (esDirectori(camiBase)) {
                System.out.println("La ruta \"" + camiBase + "\" és un directori.");
            } else {
                System.out.println("La ruta \"" + camiBase + "\" NO és un directori.");
            }

            // Comprovar si un camí és un arxiu
            if (esArxiu(camiFitxer)) {
                System.out.println("La ruta \"" + camiFitxer + "\" és un arxiu.");
            } else {
                System.out.println("La ruta \"" + camiFitxer + "\" NO és un arxiu.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
