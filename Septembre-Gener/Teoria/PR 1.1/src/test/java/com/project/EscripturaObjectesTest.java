package com.project;

import com.project.estructuresdades.Objecte;
import com.project.utilitats.UtilitatsFitxers;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

class EscripturaObjectesTest {

    @Test
    void testEscriureObjectes() {
        // Preparació
        String camiBase = System.getProperty("java.io.tmpdir") + "/testData/";
        String camiFitxer = camiBase + "ArxiuEscriuObjectesTest.dat";

        Objecte obj0 = new Objecte("Escriptori", "Estudiar");
        Objecte obj1 = new Objecte("Telèfon", "Perdre el temps");

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Escriure els objectes al fitxer
            EscripturaObjectes.escriureObjectes(camiFitxer, obj0, obj1);

            // Comprovar si l'arxiu existeix
            File fitxer = new File(camiFitxer);
            assertTrue(fitxer.exists(), "L'arxiu hauria d'existir");

            // Llegir els objectes des del fitxer binari
            try (FileInputStream fis = new FileInputStream(camiFitxer);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                Objecte llegit0 = (Objecte) ois.readObject();
                Objecte llegit1 = (Objecte) ois.readObject();

                // Comprovar que els objectes llegits són iguals als esperats
                assertEquals(obj0.getNom(), llegit0.getNom());
                assertEquals(obj0.getUtilitat(), llegit0.getUtilitat());
                assertEquals(obj1.getNom(), llegit1.getNom());
                assertEquals(obj1.getUtilitat(), llegit1.getUtilitat());
            } catch (ClassNotFoundException e) {
                fail("No s'ha pogut deserialitzar els objectes: " + e.getMessage());
            }

        } catch (IOException e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        } finally {
            // Esborrar el fitxer després del test
            new File(camiFitxer).delete();
        }
    }
}
