package com.project;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExempleNIO {

    public static void main(String[] args) {
        // Definim el camí del directori base on treballarem
        String camiBaseStr = System.getProperty("user.dir") + "/data/nio";
        Path camiBase = Paths.get(camiBaseStr);

        try {
            // Creem el directori base i tots els directoris pare si no existeixen
            // Aquesta línia és clau per assegurar que el directori 'data/nio' estigui disponible
            Files.createDirectories(camiBase);
            System.out.println("Directori base assegurat: " + camiBase);

            // Cridem el mètode que conté la lògica passant-li el directori base
            gestionarFitxerNIO(camiBase);

        } catch (IOException e) {
            System.err.println("Hi ha hagut un error greu en crear el directori base: " + e.getMessage());
        }
    }

    /**
     * Mètode que gestiona la creació, verificació i esborrat d'un fitxer d'exemple
     * dins d'un directori base proporcionat.
     * @param directoriBase El Path del directori on es realitzaran les operacions.
     */
    public static void gestionarFitxerNIO(Path directoriBase) {

        // 1. Obtenim un objecte Path que apunta al nostre fitxer dins del directori base.
        // El mètode resolve() combina el camí base amb el nom del fitxer.
        Path rutaFitxer = directoriBase.resolve("exemple-nio.txt");
        System.out.println("Operant amb el fitxer: " + rutaFitxer.toAbsolutePath());

        // 2. Intentem crear el fitxer. La gestió es fa amb un bloc try-catch.
        try {
            Files.createFile(rutaFitxer);
            System.out.println("Fitxer creat: " + rutaFitxer.getFileName());

        } catch (FileAlreadyExistsException e) {
            // Aquesta excepció salta si el fitxer ja existeix
            System.out.println("El fitxer ja existeix.");

        } catch (IOException e) {
            // Captura altres possibles errors d'entrada/sortida (p. ex. problemes de permisos)
            System.err.println("No s'ha pogut crear el fitxer: " + e.getMessage());
        }

        // 3. Verifiquem si és un fitxer, un directori o si existeix
        if (Files.isRegularFile(rutaFitxer)) {
            System.out.println("Aquesta ruta és un fitxer regular.");
        } else if (Files.isDirectory(rutaFitxer)) {
            System.out.println("Aquesta ruta és un directori.");
        }

        // 4. (Opcional) Esborrem el fitxer per poder tornar a executar l'exemple
        try {
            // El mètode deleteIfExists() no llença excepció si el fitxer no existeix
            boolean esborrat = Files.deleteIfExists(rutaFitxer);
            if (esborrat) {
                System.out.println("El fitxer de prova ha estat esborrat.");
            }
        } catch (IOException e) {
            System.err.println("No s'ha pogut esborrar el fitxer: " + e.getMessage());
        }
    }
}