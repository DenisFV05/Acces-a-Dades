package com.project;

import com.project.estructuresdades.Objecte;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.fail;

class LecturaObjectesTest {

    @TempDir
    Path directoriTemporal;  // JUnit gestionarà automàticament el directori temporal

    @Test
    void testLecturaObjectes() {
        try {
            // Preparació: Definir el nom del fitxer dins del directori temporal
            Path camiFitxer = directoriTemporal.resolve("ArxiuEscriuObjectesTest.dat");

            // Escriure dos objectes serialitzables en un fitxer binari
            try (FileOutputStream fos = new FileOutputStream(camiFitxer.toFile());
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                // Crear dos objectes d'exemple
                Objecte obj0 = new Objecte("Objecte 1", "Descripció 1");
                Objecte obj1 = new Objecte("Objecte 2", "Descripció 2");

                // Escriure els objectes al fitxer
                oos.writeObject(obj0);
                oos.writeObject(obj1);
            }

            // Execució: Cridar al mètode que llegeix i mostra els objectes del fitxer
            LecturaObjectes.llegirIMostrarObjectes(camiFitxer.toString());

        } catch (IOException e) {
            fail("El test no hauria de fallar amb excepció: " + e.getMessage());
        }
        // No cal netejar manualment el directori temporal; JUnit ho farà automàticament
    }
}
