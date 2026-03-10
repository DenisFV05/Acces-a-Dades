package com.project.utilitats;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UtilitatsFitxersTest {

    @Test
    void testCrearCarpetaSiNoExisteix() {
        // Preparació: definir el camí de la carpeta temporal
        String camiBase = System.getProperty("java.io.tmpdir") + "/testCarpeta/";

        File carpeta = new File(camiBase);

        // Comprovar que la carpeta no existeix abans de la prova
        if (carpeta.exists()) {
            carpeta.delete();
        }
        assertFalse(carpeta.exists(), "La carpeta no hauria d'existir abans de la prova");

        // Execució: intentar crear la carpeta utilitzant el mètode
        try {
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Comprovació: la carpeta hauria d'haver estat creada
            assertTrue(carpeta.exists() && carpeta.isDirectory(), "La carpeta hauria d'haver estat creada");

        } catch (IOException e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        } finally {
            // Neteja: esborrar la carpeta després del test
            carpeta.delete();
        }
    }

    @Test
    void testCrearCarpetaJaExisteix() {
        // Preparació: definir el camí de la carpeta temporal i crear-la manualment
        String camiBase = System.getProperty("java.io.tmpdir") + "/testCarpetaExisteix/";

        File carpeta = new File(camiBase);

        // Crear la carpeta manualment si no existeix
        if (!carpeta.exists()) {
            assertTrue(carpeta.mkdirs(), "La carpeta s'hauria de crear manualment per aquesta prova");
        }

        // Comprovar que la carpeta existeix abans del test
        assertTrue(carpeta.exists() && carpeta.isDirectory(), "La carpeta hauria d'existir abans de la prova");

        // Execució: intentar crear la carpeta de nou amb el mètode (ha de passar sense errors)
        try {
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Comprovació: la carpeta hauria de continuar existint sense errors i amb permisos correctes
            assertTrue(carpeta.exists() && carpeta.isDirectory(), "La carpeta hauria de continuar existint");
            assertTrue(carpeta.canRead() && carpeta.canWrite(), "El directori hauria de tenir permisos de lectura i escriptura");

        } catch (IOException e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        } finally {
            // Neteja: esborrar la carpeta després del test
            carpeta.delete();
        }
    }

    @Test
    void testErrorSiEsFitxer() {
        // Preparació: definir el camí de l'arxiu temporal
        String camiBase = System.getProperty("java.io.tmpdir") + "/testFitxer.txt";

        File fitxer = new File(camiBase);

        // Crear el fitxer manualment si no existeix
        try {
            if (!fitxer.exists()) {
                assertTrue(fitxer.createNewFile(), "El fitxer s'hauria de crear manualment per aquesta prova");
            }

            // Comprovar que el fitxer existeix abans del test
            assertTrue(fitxer.exists() && fitxer.isFile(), "El fitxer hauria d'existir abans de la prova");

            // Execució: intentar crear la carpeta amb un camí de fitxer (hauria de fallar)
            assertThrows(IOException.class, () -> UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase),
                    "Hauria de llençar una excepció perquè és un fitxer");

        } catch (IOException e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        } finally {
            // Neteja: esborrar el fitxer després del test
            fitxer.delete();
        }
    }
}
