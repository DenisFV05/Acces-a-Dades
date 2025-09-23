package com.project;

import com.project.utilitats.UtilitatsFitxers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

// Aquesta classe llegeix un arxiu de text i en mostra el contingut línia per línia.

public class LecturaArxiuList {

    // Mètode principal
    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String nomFitxer = "ArxiuList.txt";
        String camiFitxer = camiBase + nomFitxer;

        // Crear la carpeta 'data' si no existeix
        try {
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);
        } catch (IOException e) {
            System.out.println("Error en la creació de la carpeta: " + camiBase);
            e.printStackTrace();
            return;
        }

        // Llegir i mostrar les línies del fitxer
        llegirIMostrarFitxer(camiFitxer);
    }

    // Mètode per llegir i mostrar les línies d'un fitxer
    public static void llegirIMostrarFitxer(String camiFitxer) {
        try {
            List<String> linies = Files.readAllLines(Paths.get(camiFitxer), StandardCharsets.UTF_8);
            mostrarLinies(linies);
        } catch (IOException e) {
            System.out.println("Error en la lectura del fitxer: " + camiFitxer);
            e.printStackTrace();
        }
    }

    // Mètode per mostrar les línies llegides del fitxer
    public static void mostrarLinies(List<String> linies) {
        for (String linia : linies) {
            System.out.println(linia);
        }
    }
}
