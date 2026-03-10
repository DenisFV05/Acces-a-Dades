package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EscripturaRandomAccessFileWriter {

    // Injectem un directori temporal gestionat per JUnit 5. 🧑‍🔬
    @TempDir
    Path directoriTemporal;

    @Test
    void testEscriureDadesEquips_VerificaContingutCorrecte() {
        // --- PREPARACIÓ (Arrange) ---
        // Definim la ruta completa del fitxer que es crearà durant el test.
        Path rutaFitxerTest = directoriTemporal.resolve("equips-test.dat");

        // --- EXECUCIÓ (Act) ---
        // Cridem el mètode que volem provar. Aquest crearà i escriurà al fitxer.
        // Verifiquem que no llança cap excepció durant l'escriptura.
        assertDoesNotThrow(() -> {
            EscripturaRandomAccessFile.escriureDadesEquips(rutaFitxerTest);
        });

        // --- COMPROVACIÓ (Assert) ---
        // 1. Verificació bàsica: el fitxer ha d'existir.
        assertTrue(Files.exists(rutaFitxerTest), "El fitxer de dades hauria d'existir després de l'execució.");

        // 2. Verificació del contingut: llegim les dades i les comparem amb els valors originals.
        // És crucial llegir en el MATEIX ORDRE que es va escriure.
        try (RandomAccessFile raf = new RandomAccessFile(rutaFitxerTest.toFile(), "r")) { // Mode "r" -> read-only
            
            // Comprovem la mida total del fitxer abans de llegir
            assertTrue(raf.length() > 0, "El fitxer no hauria d'estar buit.");
            
            // --- Lectura i verificació de l'Equip 1 (KOI) ---
            assertEquals(true, raf.readBoolean(), "Equip 1 - 'actiu' hauria de ser true.");
            assertEquals(5, raf.readInt(), "Equip 1 - 'jugadors' hauria de ser 5.");
            assertEquals(150000.75, raf.readDouble(), "Equip 1 - 'premis' hauria de ser 150000.75.");
            assertEquals("KOI", raf.readUTF(), "Equip 1 - 'nom' hauria de ser KOI.");

            // --- Lectura i verificació de l'Equip 2 (G2 Esports) ---
            assertEquals(true, raf.readBoolean(), "Equip 2 - 'actiu' hauria de ser true.");
            assertEquals(5, raf.readInt(), "Equip 2 - 'jugadors' hauria de ser 5.");
            assertEquals(9500000.50, raf.readDouble(), "Equip 2 - 'premis' hauria de ser 9500000.50.");
            assertEquals("G2 Esports", raf.readUTF(), "Equip 2 - 'nom' hauria de ser G2 Esports.");

            // 3. Verificació final: assegurem que hem arribat al final del fitxer.
            // Això confirma que no s'han escrit dades addicionals per error.
            assertEquals(raf.length(), raf.getFilePointer(), "El punter hauria d'estar al final del fitxer després de llegir totes les dades.");

        } catch (IOException e) {
            // Si salta una excepció durant la lectura, el test ha de fallar.
            fail("No s'hauria de produir una excepció en llegir el fitxer de test: " + e.getMessage());
        }
    }
}