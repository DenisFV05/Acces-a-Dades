package com.project;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExempleNIOTest {

    /**
     * L'anotació @TempDir de JUnit 5 ens injecta un Path
     * que apunta a un directori temporal únic per a aquest test.
     * JUnit s'encarrega de crear-lo abans i d'esborrar-lo automàticament després,
     * la qual cosa és ideal per a proves d'entrada/sortida de fitxers.
     */
    @TempDir
    Path directoriTemporal;

    @Test
    void testGestionarFitxerNIO_CreaIEsborraCorrectament() {
        // --- PREPARACIÓ (Arrange) ---
        // El nom del fitxer que el mètode gestionarFitxerNIO crearà internament.
        String nomFitxer = "exemple-nio.txt";
        Path rutaFitxerEsperat = directoriTemporal.resolve(nomFitxer);

        // Verifiquem l'estat inicial: el fitxer no ha d'existir abans de cridar el mètode.
        assertFalse(Files.exists(rutaFitxerEsperat), "El fitxer no hauria d'existir abans de l'execució del test.");

        // --- EXECUCIÓ (Act) ---
        // Cridem el mètode a provar, passant-li el nostre directori temporal.
        // Amb assertDoesNotThrow verifiquem que el mètode s'executa completament
        // sense llançar cap excepció inesperada (com una IOException).
        assertDoesNotThrow(() -> {
            ExempleNIO.gestionarFitxerNIO(directoriTemporal);
        });

        // --- COMPROVACIÓ (Assert) ---
        // El mètode gestiona tot el cicle de vida: crea el fitxer i després l'esborra.
        // Per tant, la comprovació final és assegurar-nos que el fitxer ja NO existeix,
        // la qual cosa significa que la part d'esborrat ha funcionat correctament.
        assertFalse(Files.exists(rutaFitxerEsperat), "El fitxer hauria d'haver estat esborrat al final de l'execució.");
    }
}