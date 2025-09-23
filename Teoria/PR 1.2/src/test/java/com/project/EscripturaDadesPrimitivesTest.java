package com.project;

import com.project.utilitats.UtilitatsFitxers;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class EscripturaDadesPrimitivesTest {

    @Test
    void testEscriureDadesPrimitives() {
        // Preparació
        String camiBase = System.getProperty("java.io.tmpdir") + "/testData/";
        String camiFitxer = camiBase + "ArxiuEscriuPrimitivesTest.dat";

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Escriure dades primitives
            EscripturaDadesPrimitives.escriureDadesPrimitives(camiFitxer, "Hola", 44, true, 'A', 2.46);

            // Comprovar si l'arxiu existeix
            File fitxer = new File(camiFitxer);
            assertTrue(fitxer.exists(), "L'arxiu hauria d'existir");

            // Llegir les dades per validar
            try (FileInputStream fis = new FileInputStream(camiFitxer);
                 DataInputStream dis = new DataInputStream(fis)) {

                // Comprovar les dades primitives
                assertEquals("Hola", dis.readUTF());
                assertEquals(44, dis.readInt());
                assertTrue(dis.readBoolean());
                assertEquals('A', dis.readChar());
                assertEquals(2.46, dis.readDouble());
            }

        } catch (IOException e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        } finally {
            // Esborrar el fitxer després del test
            new File(camiFitxer).delete();
        }
    }
}
