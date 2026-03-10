package com.project;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        Path path = Paths.get(rutaArxiu);
        File arxiu = path.toFile();

        // Comprovar si el path és una carpeta
        if (arxiu.isDirectory()) {
            System.out.println("El path no correspon a un arxiu, sinó a una carpeta.");
            return;
        }

        // Comprovar si el fitxer existeix i mostrar el contingut amb codificació UTF-8
        if (arxiu.isFile()) {
            try {
                Files.lines(path, StandardCharsets.UTF_8).forEach(System.out::println);
            } catch (IOException e) {
                System.out.println("S'ha produït un error en llegir l'arxiu: " + e.getMessage());
            }
        } else {
            System.out.println("El fitxer no existeix o no és accessible.");
        }
    }
}
