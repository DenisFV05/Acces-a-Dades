package com.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LecturaFileChannelTest {

    @TempDir
    Path directoriTemporal;

    private Path fitxerTest;

    @BeforeEach
    void setUp() throws IOException {
        fitxerTest = directoriTemporal.resolve("equips-test.dat");
        // Creem el fitxer de proves abans de cada test
        EscripturaFileChannel.escriureEquips(fitxerTest);
    }

    @Test
    void testLlegirEquips_RetornaLlistaCorrecta() {
        // Act
        List<Equip> equipsLlegits = assertDoesNotThrow(
            () -> LecturaFileChannel.llegirEquips(fitxerTest)
        );

        // Assert
        assertNotNull(equipsLlegits);
        assertEquals(2, equipsLlegits.size());

        // Verifiquem el contingut de l'Equip 1 (KOI)
        Equip equip1 = equipsLlegits.get(0);
        assertEquals("KOI", equip1.nom());
        assertTrue(equip1.actiu());
        assertEquals(5, equip1.jugadors());
        assertEquals(150000.75, equip1.premis());

        // Verifiquem el contingut de l'Equip 2 (G2 Esports)
        Equip equip2 = equipsLlegits.get(1);
        assertEquals("G2 Esports", equip2.nom());
        assertTrue(equip2.actiu());
        assertEquals(5, equip2.jugadors());
        assertEquals(9500000.50, equip2.premis());
    }
}