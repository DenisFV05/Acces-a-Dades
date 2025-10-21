package com.project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PR113append {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiFitxer = System.getProperty("user.dir") + "/data/frasesMatrix.txt";

        // Crida al mètode que afegeix les frases al fitxer
        afegirFrases(camiFitxer);
    }

    // Mètode que afegeix les frases al fitxer amb UTF-8 i línia en blanc final
    public static void afegirFrases(String camiFitxer) {
        // Frases de Matrix
        String frase1 = "I can only show you the door";
        String frase2 = "You're the one that has to walk through it";

        // Afegir les frases al final del fitxer amb UTF-8
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(camiFitxer, StandardCharsets.UTF_8, true))) {
            writer.write(frase1);
            writer.newLine();
            writer.write(frase2);
            writer.newLine();  // Afegir una línia en blanc al final
        } catch (IOException e) {
            System.out.println("Error en escriure l'arxiu: " + e.getMessage());
        }
    }
}
