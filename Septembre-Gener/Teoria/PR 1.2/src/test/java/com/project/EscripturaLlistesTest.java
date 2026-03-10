package com.project;

import com.project.estructuresdades.Objecte;
import com.project.utilitats.UtilitatsFitxers;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

class EscripturaLlistesTest {

    @Test
    void testEscriureLlistaObjectes() {
        // Preparació
        String camiBase = System.getProperty("java.io.tmpdir") + "/testData/";
        String camiFitxer = camiBase + "ArxiuEscriuLlistesTest.dat";

        List<Objecte> llistaEsperada = new ArrayList<>();
        llistaEsperada.add(new Objecte("Escriptori", "Estudiar"));
        llistaEsperada.add(new Objecte("Telèfon", "Perdre el temps"));

        try {
            // Utilitzar la classe utilitat per crear la carpeta si no existeix
            UtilitatsFitxers.crearCarpetaSiNoExisteix(camiBase);

            // Escriure la llista d'objectes
            EscripturaLlistes.escriureLlistaObjectes(camiFitxer, llistaEsperada);

            // Comprovar si l'arxiu existeix
            File fitxer = new File(camiFitxer);
            assertTrue(fitxer.exists(), "L'arxiu hauria d'existir");

            // Llegir la llista des del fitxer binari
            try (FileInputStream fis = new FileInputStream(camiFitxer);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                Objecte[] llistaLlegida = (Objecte[]) ois.readObject();
                
                // Comprovar que la llista llegida és igual a l'esperada
                assertEquals(llistaEsperada.size(), llistaLlegida.length);
                for (int i = 0; i < llistaEsperada.size(); i++) {
                    assertEquals(llistaEsperada.get(i).getNom(), llistaLlegida[i].getNom());
                    assertEquals(llistaEsperada.get(i).getUtilitat(), llistaLlegida[i].getUtilitat());
                }
            } catch (ClassNotFoundException e) {
                fail("No s'ha pogut deserialitzar la llista d'objectes: " + e.getMessage());
            }

        } catch (IOException e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        } finally {
            // Esborrar el fitxer després del test
            new File(camiFitxer).delete();
        }
    }
}
