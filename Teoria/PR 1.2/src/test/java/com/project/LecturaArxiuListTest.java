package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LecturaArxiuListTest {

    @TempDir
    File directoriTemporal;  // JUnit gestiona automàticament la creació i eliminació del directori temporal

    @Test
    void testLecturaArxiu() {
        // Preparació: Utilitzar el directori temporal proporcionat per JUnit
        String nomFitxer = "ArxiuTestLectura.txt";
        File fitxer = new File(directoriTemporal, nomFitxer);
        String camiFitxer = fitxer.getPath();

        try {
            // Escriure contingut al fitxer de prova
            List<String> contingutEsperat = List.of(
                "Aquesta és la primera línia.",
                "Aquesta és la segona línia.",
                "Aquesta és la tercera línia."
            );
            Files.write(fitxer.toPath(), contingutEsperat, StandardCharsets.UTF_8);

            // Cridar al mètode que llegeix i mostra el fitxer
            LecturaArxiuList.llegirIMostrarFitxer(camiFitxer);

            // Llegir el contingut del fitxer per assegurar que s'ha llegit correctament
            List<String> contingutLlistat = Files.readAllLines(fitxer.toPath(), StandardCharsets.UTF_8);
            assertEquals(contingutEsperat, contingutLlistat, "El contingut llegit hauria de coincidir amb el contingut esperat");

        } catch (IOException e) {
            fail("No hauria de fallar amb una excepció: " + e.getMessage());
        }
    }
}
