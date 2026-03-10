package com.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LecturaRandomAccessFileTest {

    // Directori temporal gestionat per JUnit
    @TempDir
    Path directoriTemporal;

    private Path rutaFitxerTest;

    /**
     * Aquest mètode s'executa ABANS de cada test (@Test).
     * La seva funció és crear un fitxer de dades net i fiable per a la prova.
     */
    @BeforeEach
    void setUp() throws IOException {
        rutaFitxerTest = directoriTemporal.resolve("equips-per-test.dat");
        // Cridem l'escriptor per generar el fitxer que el nostre lector ha de provar.
        // Això garanteix que el test és autònom.
        EscripturaRandomAccessFile.escriureDadesEquips(rutaFitxerTest);
    }

    @Test
    void testLlegirDadesEquips_RetornaLlistaCorrecta() {
        // --- PREPARACIÓ (Arrange) ---
        // El mètode setUp() ja ha preparat el fitxer.

        // --- EXECUCIÓ (Act) ---
        // Cridem el mètode de lectura a provar i guardem el resultat.
        List<Equip> equipsLlegits = assertDoesNotThrow(
            () -> LecturaRandomAccessFile.llegirDadesEquips(rutaFitxerTest),
            "La lectura del fitxer no hauria de llançar una excepció."
        );
        
        // --- COMPROVACIÓ (Assert) ---
        // 1. Verifiquem la mida de la llista
        assertNotNull(equipsLlegits, "La llista no hauria de ser nul·la.");
        assertEquals(2, equipsLlegits.size(), "S'haurien d'haver llegit exactament 2 equips.");

        // 2. Verifiquem el contingut del primer equip (KOI)
        Equip equip1 = equipsLlegits.get(0);
        assertEquals("KOI", equip1.nom());
        assertTrue(equip1.actiu());
        assertEquals(5, equip1.jugadors());
        assertEquals(150000.75, equip1.premis());

        // 3. Verifiquem el contingut del segon equip (G2 Esports)
        Equip equip2 = equipsLlegits.get(1);
        assertEquals("G2 Esports", equip2.nom());
        assertTrue(equip2.actiu());
        assertEquals(5, equip2.jugadors());
        assertEquals(9500000.50, equip2.premis());
    }
}