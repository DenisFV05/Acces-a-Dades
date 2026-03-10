package com.project;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class PR115cp {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error: Has d'indicar dues rutes d'arxiu.");
            System.out.println("Ús: PR115cp <origen> <destinació>");
            return;
        }

        // Ruta de l'arxiu origen
        String rutaOrigen = args[0];
        // Ruta de l'arxiu destinació
        String rutaDesti = args[1];

        // Crida al mètode per copiar l'arxiu
        copiarArxiu(rutaOrigen, rutaDesti);
    }

    // Mètode per copiar un arxiu de text de l'origen al destí
    public static void copiarArxiu(String rutaOrigen, String rutaDesti) {
        Path origen = Paths.get(rutaOrigen);
        Path desti = Paths.get(rutaDesti);

        // Verificar existencia del archivo origen
        if (!Files.exists(origen) || !Files.isRegularFile(origen)) {
            System.out.println("Error: El fitxer d'origen no existeix o no és un fitxer vàlid.");
            return;
        }

        // Avisar si el archivo destino ya existe
        if (Files.exists(desti)) {
            System.out.println("Advertència: El fitxer de destinació existeix i serà sobreescrit.");
        }

        try {
            // Leer líneas del archivo origen
            List<String> lineas = Files.readAllLines(origen, StandardCharsets.UTF_8);

            // Escribir líneas al archivo destino
            Files.write(desti, lineas, StandardCharsets.UTF_8);

            System.out.println("Còpia realitzada correctament.");
        } catch (IOException e) {
            System.out.println("Error durant la còpia: " + e.getMessage());
        }
    }
}
