package com.project;

import com.project.utilitats.UtilitatsFitxers;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class EscripturaArxiuWriterTest {

    @Test
    void testEscriureIAfegirFitxer() {
        // Preparació
        String camiBase = System.getProperty("java.io.tmpdir") + "/testData/";
        String camiFitxer = camiBase + "ArxiuEscriuTest.txt";

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Escriure contingut inicial
            EscripturaArxiuWriter.escriureFitxer(camiFitxer, "Sometimes life hits you in the head with a brick\n"
                    + "Don’t lose faith. I’m convinced that the only\n");

            // Afegir contingut addicional
            EscripturaArxiuWriter.afegirAlFitxer(camiFitxer, "thing that kept me going was that I loved what I did.\n"
                    + "You’ve got to find what you love.\n");

            // Comprovar si l'arxiu existeix
            File fitxer = new File(camiFitxer);
            assertTrue(fitxer.exists(), "L'arxiu hauria d'existir");

            // Comprovar el contingut de l'arxiu
            List<String> llegides = Files.readAllLines(Path.of(camiFitxer));
            List<String> esperades = List.of(
                "Sometimes life hits you in the head with a brick",
                "Don’t lose faith. I’m convinced that the only",
                "thing that kept me going was that I loved what I did.",
                "You’ve got to find what you love."
            );
            assertEquals(esperades, llegides, "Les línies haurien de coincidir");

        } catch (IOException e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        } finally {
            // Esborrar el fitxer després del test
            new File(camiFitxer).delete();
        }
    }
}
