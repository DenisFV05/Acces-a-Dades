package com.project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

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

        // Comprovar si l'arxiu origen existeix i és un arxiu regular
        if (!Files.exists(origen) || !Files.isRegularFile(origen)) {
            System.out.println("Error: L'arxiu d'origen no existeix o no és un arxiu de text.");
            return;
        }

        // Comprovar si l'arxiu destí ja existeix
        if (Files.exists(desti)) {
            System.out.println("Advertència: L'arxiu de destí ja existeix i serà sobreescrit.");
        }

        // Realitzar la còpia de l'arxiu origen al destí
        try (BufferedReader reader = Files.newBufferedReader(origen, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(desti, StandardCharsets.UTF_8)) {

            String linia;
            while ((linia = reader.readLine()) != null) {
                writer.write(linia);
                writer.newLine(); // Afegir un salt de línia després de cada línia
            }

            // Comprovar si el fitxer acaba amb una línia en blanc i afegir-la si cal
            if (Files.size(origen) > 0) {
                long totalLines = Files.lines(origen, StandardCharsets.UTF_8).count();
                if (Files.lines(origen).skip(totalLines - 1).findFirst().get().isEmpty()) {
                    writer.newLine(); // Afegir línia en blanc si l'última línia estava en blanc
                }
            }

            System.out.println("La còpia s'ha realitzat correctament.");

        } catch (IOException e) {
            System.out.println("Error en copiar l'arxiu: " + e.getMessage());
        }
    }
}
