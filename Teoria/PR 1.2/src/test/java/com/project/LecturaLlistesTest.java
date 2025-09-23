package com.project;

import com.project.estructuresdades.Objecte;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

class LecturaLlistesTest {

    @TempDir
    Path directoriTemporal;  // JUnit gestionarà automàticament el directori temporal

    @Test
    void testLecturaLlistes() {
        try {
            // Preparació: Definir el nom del fitxer dins del directori temporal
            Path camiFitxer = directoriTemporal.resolve("ArxiuEscriuLlistesTest.dat");

            // Escriure una llista d'objectes serialitzables en un fitxer binari
            try (FileOutputStream fos = new FileOutputStream(camiFitxer.toFile());
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                // Crear una llista d'objectes d'exemple
                ArrayList<Objecte> llistaObjectes = new ArrayList<>();
                llistaObjectes.add(new Objecte("Objecte 1", "Descripció 1"));
                llistaObjectes.add(new Objecte("Objecte 2", "Descripció 2"));

                // Escriure la llista al fitxer
                oos.writeObject(llistaObjectes.toArray(new Objecte[0]));
            }

            // Execució: Cridar al mètode que llegeix i llista els objectes del fitxer
            LecturaLlistes.llegirILlistarObjectes(camiFitxer.toString());

        } catch (IOException e) {
            fail("El test no hauria de fallar amb excepció: " + e.getMessage());
        }
        // No cal netejar manualment el directori temporal; JUnit ho farà automàticament
    }
}
