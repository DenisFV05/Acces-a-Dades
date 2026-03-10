package com.project;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExempleNIO {
    public static void main(String[] args) {

        // 1. Obtenim un objecte Path que apunta a un fitxer (equivalent a new File())
        Path ruta = Paths.get("exemple-nio.txt");

        // 2. Intentem crear el fitxer. La gestió es fa amb un bloc try-catch.
        try {
            Files.createFile(ruta);
            System.out.println("Fitxer creat: " + ruta.getFileName());

        } catch (FileAlreadyExistsException e) {
            // Aquesta excepció salta si el fitxer ja existeix
            System.out.println("El fitxer ja existeix.");

        } catch (IOException e) {
            // Captura altres possibles errors d'entrada/sortida (p. ex. problemes de permisos)
            System.err.println("No s'ha pogut crear el fitxer: " + e.getMessage());
        }

        // 3. Verifiquem si és un fitxer, un directori o si existeix
        if (Files.isRegularFile(ruta)) {
            System.out.println("Aquesta ruta és un fitxer regular.");
        } else if (Files.isDirectory(ruta)) {
            System.out.println("Aquesta ruta és un directori.");
        }

        // 4. (Opcional) Esborrem el fitxer per poder tornar a executar l'exemple
        try {
            // El mètode deleteIfExists() no llença excepció si el fitxer no existeix
            boolean esborrat = Files.deleteIfExists(ruta);
            if (esborrat) {
                System.out.println("El fitxer de prova ha estat esborrat.");
            }
        } catch (IOException e) {
            System.err.println("No s'ha pogut esborrar el fitxer: " + e.getMessage());
        }
    }
}