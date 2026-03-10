package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LecturaArxiuScannerTest {

    @TempDir
    Path directoriTemporal;  // JUnit gestionarà automàticament el directori temporal

    @Test
    void testLecturaArxiu() {
        // Preparació: Crear el fitxer dins del directori temporal
        String nomFitxer = "ArxiuTestLectura.txt";
        Path camiFitxer = directoriTemporal.resolve(nomFitxer);

        // Contingut esperat del fitxer
        List<String> contingutEsperat = List.of(
            "Primera línia.",
            "Segona línia.",
            "Tercera línia amb caràcters especials: àèíòù"
        );

        try {
            // Escriure contingut al fitxer de prova amb codificació UTF-8
            Files.write(camiFitxer, contingutEsperat, StandardCharsets.UTF_8);

            // Execució: Cridar al mètode que llegeix i mostra el fitxer
            LecturaArxiuScanner.llegirIMostrarFitxer(camiFitxer.toString());

            // Verificació: Llegir el contingut del fitxer per assegurar que s'ha llegit correctament
            List<String> contingutLlistat = Files.readAllLines(camiFitxer, StandardCharsets.UTF_8);
            assertEquals(contingutEsperat, contingutLlistat, "El contingut llegit hauria de coincidir amb el contingut esperat");

        } catch (IOException e) {
            fail("No hauria de fallar amb una excepció: " + e.getMessage());
        }
        // No cal netejar manualment el directori temporal; JUnit ho farà automàticament
    }
}
