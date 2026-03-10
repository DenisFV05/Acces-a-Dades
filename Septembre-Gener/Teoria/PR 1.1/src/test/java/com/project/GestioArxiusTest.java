package com.project;

import com.project.utilitats.UtilitatsFitxers;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;

class GestioArxiusTest {

    @Test
    void testGestioArxius() {
        // Preparació
        String camiBase = System.getProperty("java.io.tmpdir") + "/testData/";
        String camiFitxerTmp = camiBase + "ArxiuTmp.txt";

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Crear un arxiu temporal i comprovar que existeix
            boolean creat = GestioArxius.crearArxiu(camiFitxerTmp);
            assertTrue(creat, "L'arxiu temporal s'hauria d'haver creat");

            // Comprovar que l'arxiu existeix
            assertTrue(GestioArxius.comprovarExistenciaArxiu(camiFitxerTmp), "L'arxiu hauria d'existir");

            // Comprovar que la ruta és un directori
            assertTrue(GestioArxius.esDirectori(camiBase), "La ruta hauria de ser un directori");

            // Comprovar que la ruta és un arxiu
            assertTrue(GestioArxius.esArxiu(camiFitxerTmp), "La ruta hauria de ser un arxiu");

            // Esborrar l'arxiu temporal i comprovar que s'ha esborrat
            boolean esborrat = GestioArxius.esborrarArxiu(camiFitxerTmp);
            assertTrue(esborrat, "L'arxiu temporal s'hauria d'haver esborrat");

            // Comprovar que l'arxiu ja no existeix
            assertFalse(GestioArxius.comprovarExistenciaArxiu(camiFitxerTmp), "L'arxiu no hauria d'existir");

        } catch (IOException e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        }
    }
}
