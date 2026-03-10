package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import static org.junit.jupiter.api.Assertions.*;

class EscripturaFileChannelTest {

    @TempDir
    Path directoriTemporal;

    @Test
    void testEscriureEquips_ContingutCorrecte() throws IOException {
        // Arrange
        Path fitxerTest = directoriTemporal.resolve("equips-test.dat");

        // Act
        assertDoesNotThrow(() -> EscripturaFileChannel.escriureEquips(fitxerTest));

        // Assert
        assertTrue(Files.exists(fitxerTest));

        // Llegim el fitxer per verificar el seu contingut byte a byte
        try (FileChannel channel = FileChannel.open(fitxerTest, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
            channel.read(buffer);
            buffer.flip();

            // Verifiquem Equip 1 (KOI)
            assertEquals(1, buffer.get()); // actiu = true
            assertEquals(5, buffer.getInt());
            assertEquals(150000.75, buffer.getDouble());
            int longNom1 = buffer.getInt();
            byte[] nomBytes1 = new byte[longNom1];
            buffer.get(nomBytes1);
            assertEquals("KOI", new String(nomBytes1, java.nio.charset.StandardCharsets.UTF_8));

            // Verifiquem Equip 2 (G2 Esports)
            assertEquals(1, buffer.get()); // actiu = true
            assertEquals(5, buffer.getInt());
            assertEquals(9500000.50, buffer.getDouble());
            int longNom2 = buffer.getInt();
            byte[] nomBytes2 = new byte[longNom2];
            buffer.get(nomBytes2);
            assertEquals("G2 Esports", new String(nomBytes2, java.nio.charset.StandardCharsets.UTF_8));
            
            assertFalse(buffer.hasRemaining()); // Ens assegurem que no hi ha més dades
        }
    }
}