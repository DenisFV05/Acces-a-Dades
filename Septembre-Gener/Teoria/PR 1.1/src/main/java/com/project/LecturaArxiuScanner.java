package com.project;

import com.project.utilitats.UtilitatsFitxers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Aquesta classe llegeix un fitxer de text línia a línia utilitzant Scanner.
public class LecturaArxiuScanner {

    // Mètode principal
    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String nomFitxer = "ArxiuWriter.txt";
        String camiFitxer = camiBase + nomFitxer;

        // Crear la carpeta 'data' si no existeix
        try {
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);
        } catch (Exception e) {
            System.out.println("Error en la creació de la carpeta: " + camiBase);
            e.printStackTrace();
            return;
        }

        // Llegir el fitxer i mostrar el contingut
        llegirIMostrarFitxer(camiFitxer);
    }

    // Mètode per llegir i mostrar el contingut d'un fitxer línia a línia
    public static void llegirIMostrarFitxer(String camiFitxer) {
        File fitxer = new File(camiFitxer);
        try (Scanner scanner = new Scanner(fitxer)) {
            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                System.out.println(linia);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fitxer no trobat: " + camiFitxer);
            e.printStackTrace();
        }
    }
}
