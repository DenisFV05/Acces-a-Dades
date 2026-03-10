package com.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class PR112cat {

    public static void main(String[] args) {
        // Comprovar que s'ha proporcionat una ruta com a paràmetre
        if (args.length == 0) {
            System.out.println("No s'ha proporcionat cap ruta d'arxiu.");
            return;
        }

        // Obtenir la ruta del fitxer des dels paràmetres
        String rutaArxiu = args[0];
        mostrarContingutArxiu(rutaArxiu);
    }

    // Funció per mostrar el contingut de l'arxiu o el missatge d'error corresponent
    public static void mostrarContingutArxiu(String rutaArxiu) {
        Path fitxer = Paths.get(rutaArxiu);
        if (Files.exists(fitxer)) {
            if (Files.isDirectory(fitxer)) {
                System.out.println("El path no correspon a un arxiu, sinó a una carpeta.");
                return; // Error si el archivo es una carpeta
            }

            try {
                Files.lines(fitxer, StandardCharsets.UTF_8).forEach(System.out::println); // Imprime cada linea
            } catch (IOException e) { // Error si el archivo no existe o otro problema
                System.out.println("El fitxer no existeix o no és accessible.");
            }

        } else {
            System.out.println("El fitxer no existeix o no és accessible.");
        }
    }
}
