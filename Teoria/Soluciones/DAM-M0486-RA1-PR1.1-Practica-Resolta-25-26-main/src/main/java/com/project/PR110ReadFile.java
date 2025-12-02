package com.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PR110ReadFile {

    public static void main(String[] args) {
        String camiFitxer = System.getProperty("user.dir") + "/data/GestioTasques.java";
        llegirIMostrarFitxer(camiFitxer);  // Només cridem a la funció amb la ruta del fitxer
    }

    // Funció que llegeix el fitxer i mostra les línies amb numeració
    public static void llegirIMostrarFitxer(String camiFitxer) {
        try {
            List<String> linies = Files.readAllLines(Paths.get(camiFitxer));
            mostrarLiniesAmbNumeracio(linies);
        } catch (IOException e) {
            System.out.println("Error en la lectura del fitxer: " + camiFitxer);
            e.printStackTrace();
        }
    }

    // Funció que mostra les línies amb numeració
    public static void mostrarLiniesAmbNumeracio(List<String> linies) {
        int numeroLinia = 1;
        for (String linia : linies) {
            System.out.printf("%d: %s%n", numeroLinia++, linia);
        }
    }
}
