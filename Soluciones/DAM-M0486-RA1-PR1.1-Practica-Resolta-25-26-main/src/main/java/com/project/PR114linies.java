package com.project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class PR114linies {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiFitxer = System.getProperty("user.dir") + "/data/numeros.txt";

        // Crida al mètode que genera i escriu els números aleatoris
        generarNumerosAleatoris(camiFitxer);
    }

    // Mètode per generar 10 números aleatoris i escriure'ls al fitxer
    public static void generarNumerosAleatoris(String camiFitxer) {
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(camiFitxer, StandardCharsets.UTF_8))) {
            for (int i = 0; i < 10; i++) {
                int numero = random.nextInt(100);  // Generar un número aleatori entre 0 i 99
                writer.write(Integer.toString(numero));

                // Afegir salt de línia només si no és l'últim número
                if (i < 9) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error en escriure l'arxiu: " + e.getMessage());
        }
    }
}
