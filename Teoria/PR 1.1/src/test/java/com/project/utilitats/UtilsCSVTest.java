package com.project.utilitats;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

class UtilsCSVTest {

    @Test
    void testLlegirIEscriureCSV() {
        // Preparació
        String camiBase = System.getProperty("java.io.tmpdir") + "/testData/";
        String camiFitxer = camiBase + "ArxiuTest.csv";

        List<String> csvInicial = Arrays.asList(
            "id,titol,any",
            "123,Avatar,2009",
            "456,Titanic,1997"
        );

        try {
            // Escriure les dades inicials al fitxer
            UtilsCSV.escriure(camiFitxer, csvInicial);

            // Llegir el fitxer CSV
            List<String> csvLlegit = UtilsCSV.llegir(camiFitxer);
            assertNotNull(csvLlegit, "La llista CSV no hauria de ser nul·la");
            assertEquals(csvInicial, csvLlegit, "Les dades llegides haurien de coincidir amb les escrites");

        } catch (Exception e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        } finally {
            // Neteja: esborrar el fitxer després del test
            new File(camiFitxer).delete();
        }
    }

    @Test
    void testObtenirColumnesIActualitzar() {
        // Preparació
        String camiBase = System.getProperty("java.io.tmpdir") + "/testData/";
        String camiFitxer = camiBase + "ArxiuTest.csv";

        List<String> csvInicial = Arrays.asList(
            "id,titol,any",
            "123,Avatar,2009",
            "456,Titanic,1997"
        );

        try {
            // Escriure les dades inicials al fitxer
            UtilsCSV.escriure(camiFitxer, csvInicial);

            // Llegir el fitxer CSV
            List<String> csvLlegit = UtilsCSV.llegir(camiFitxer);

            // Obtenir les columnes
            String[] columnes = UtilsCSV.obtenirClaus(csvLlegit);
            assertArrayEquals(new String[]{"id", "titol", "any"}, columnes, "Les columnes haurien de coincidir");

            // Actualitzar l'any de la pel·lícula "Avatar"
            int numLiniaAvatar = UtilsCSV.obtenirNumLinia(csvLlegit, "id", "123");
            UtilsCSV.actualitzarLinia(csvLlegit, numLiniaAvatar, "any", "2021");

            // Comprovar l'actualització
            String[] dadesAvatar = UtilsCSV.obtenirArrayLinia(csvLlegit.get(numLiniaAvatar));
            assertEquals("2021", dadesAvatar[2], "L'any hauria d'haver estat actualitzat a 2021");

            // Escriure les dades actualitzades
            UtilsCSV.escriure(camiFitxer, csvLlegit);

            // Llegir de nou i comprovar l'actualització
            List<String> csvFinal = Files.readAllLines(Path.of(camiFitxer));
            assertTrue(csvFinal.contains("123,Avatar,2021"), "El CSV hauria de contenir Avatar amb l'any actualitzat");

        } catch (Exception e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        } finally {
            // Neteja: esborrar el fitxer després del test
            new File(camiFitxer).delete();
        }
    }
}
