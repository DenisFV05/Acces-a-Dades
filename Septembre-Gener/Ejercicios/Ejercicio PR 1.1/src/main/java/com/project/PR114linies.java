package com.project;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
        Path fitxer = Paths.get(camiFitxer);
        Random rand = new Random();
        List<String> numeros = new ArrayList<>();
        // Bucle de randoms
        for (int i = 0; i < 10; i++) {
            numeros.add(String.valueOf(rand.nextInt(100))); // 0 a 99, int a String y se añade a la lista
        }
        // Uso de StringBuilder para escribir con utf8
        StringBuilder contenido = new StringBuilder();
        for (int i = 0; i < numeros.size(); i++) {
            contenido.append(numeros.get(i)); // Añadir los numeros random a StringB
            if (i != numeros.size() - 1) { // En el ultimo num no hay salto de linea
                contenido.append(System.lineSeparator());
            }
            
        try {
            Files.write(fitxer, contenido.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("Archivo 'numeros.txt' generado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }

        }
    }
}
